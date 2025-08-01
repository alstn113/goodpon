package com.goodpon.api.partner.application.coupon

import com.goodpon.api.partner.support.AbstractIntegrationTest
import com.goodpon.api.partner.support.accessor.TestCouponHistoryAccessor
import com.goodpon.api.partner.support.accessor.TestCouponTemplateAccessor
import com.goodpon.api.partner.support.accessor.TestMerchantAccessor
import com.goodpon.api.partner.support.accessor.TestUserCouponAccessor
import com.goodpon.application.partner.coupon.port.`in`.dto.IssueCouponCommand
import com.goodpon.application.partner.coupon.port.`in`.dto.RedeemCouponCommand
import com.goodpon.application.partner.coupon.port.out.CouponTemplateStatsCache
import com.goodpon.application.partner.coupon.service.IssueCouponService
import com.goodpon.application.partner.coupon.service.RedeemCouponService
import com.goodpon.domain.coupon.history.CouponActionType
import com.goodpon.domain.coupon.template.exception.CouponTemplateRedemptionLimitExceededException
import com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType
import com.goodpon.domain.coupon.user.UserCouponStatus
import com.goodpon.domain.coupon.user.exception.UserCouponAlreadyRedeemedException
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicInteger

class RedeemCouponServiceConcurrencyIT(
    private val redeemCouponService: RedeemCouponService,
    private val issueCouponService: IssueCouponService,
    private val testMerchantAccessor: TestMerchantAccessor,
    private val testUserCouponAccessor: TestUserCouponAccessor,
    private val testCouponTemplateAccessor: TestCouponTemplateAccessor,
    private val testCouponHistoryAccessor: TestCouponHistoryAccessor,
    private val couponTemplateStatsCache: CouponTemplateStatsCache,
) : AbstractIntegrationTest() {

    @Test
    fun `여러 사용자가 동시에 쿠폰 사용 시 사용 제한을 초과하지 않는다`(): Unit = runBlocking {
        // given
        val issueCount = 10L
        val maxRedeemCount = 5L
        val (merchantId) = testMerchantAccessor.createMerchant()
        val couponTemplateId = testCouponTemplateAccessor.createCouponTemplate(
            merchantId = merchantId,
            limitType = CouponLimitPolicyType.REDEEM_COUNT,
            maxRedeemCount = maxRedeemCount,
            maxIssueCount = null,
            minOrderAmount = 10000,
        )
        val userIds = (1..issueCount).map { index -> "user-$index" }
        val userIdToCouponIdMap: Map<String, String> = userIds.associateWith { userId ->
            val command = IssueCouponCommand(
                merchantId = merchantId,
                couponTemplateId = couponTemplateId,
                userId = userId
            )
            val issuedCoupon = issueCouponService(command)
            issuedCoupon.userCouponId
        }

        val successCount = AtomicInteger(0)
        val failureCount = AtomicInteger(0)

        // when
        coroutineScope {
            val jobs = userIds.map { userId ->
                launch(Dispatchers.IO) {
                    try {
                        val userCouponId = userIdToCouponIdMap[userId]!!
                        val command = RedeemCouponCommand(
                            merchantId = merchantId,
                            userCouponId = userCouponId,
                            userId = userId,
                            orderAmount = 15000,
                            orderId = "unique-order-id-$userId"
                        )
                        redeemCouponService(command)
                        successCount.incrementAndGet()
                    } catch (e: CouponTemplateRedemptionLimitExceededException) {
                        failureCount.incrementAndGet()
                    }
                }
            }
            jobs.joinAll()
        }

        // then
        successCount.get() shouldBe maxRedeemCount
        failureCount.get() shouldBe issueCount - maxRedeemCount

        val foundUserCouponEntities = testUserCouponAccessor.findByCouponTemplateId(couponTemplateId)
        foundUserCouponEntities.size shouldBe issueCount
        foundUserCouponEntities.count { it.status == UserCouponStatus.REDEEMED } shouldBe maxRedeemCount
        foundUserCouponEntities.count { it.status == UserCouponStatus.ISSUED } shouldBe issueCount - maxRedeemCount

        couponTemplateStatsCache.getStats(couponTemplateId).let {
            it.first shouldBe issueCount
            it.second shouldBe maxRedeemCount
        }

        val foundHistoryEntities = testCouponHistoryAccessor.findAll()
        foundHistoryEntities.size shouldBe issueCount + maxRedeemCount
        foundHistoryEntities.count { it.actionType == CouponActionType.ISSUE } shouldBe issueCount
        foundHistoryEntities.count { it.actionType == CouponActionType.REDEEM } shouldBe maxRedeemCount
    }

    @Test
    fun `동일한 사용자가 동일한 쿠폰을 동시에 사용 시 하나만 사용된다`(): Unit = runBlocking {
        // given
        val (merchantId) = testMerchantAccessor.createMerchant()
        val couponTemplateId = testCouponTemplateAccessor.createCouponTemplate(
            merchantId = merchantId,
            limitType = CouponLimitPolicyType.REDEEM_COUNT,
            maxRedeemCount = 10L,
            maxIssueCount = null,
            minOrderAmount = 10000,
        )
        val userId = "user-unique"
        val issueCommand = IssueCouponCommand(
            merchantId = merchantId,
            couponTemplateId = couponTemplateId,
            userId = userId
        )
        val issuedCoupon = issueCouponService(issueCommand)

        val concurrentRequests = 5
        val successCount = AtomicInteger(0)
        val failureCount = AtomicInteger(0)

        // when
        coroutineScope {
            val jobs = List(concurrentRequests) { index ->
                launch(Dispatchers.IO) {
                    try {
                        val command = RedeemCouponCommand(
                            merchantId = merchantId,
                            userCouponId = issuedCoupon.userCouponId,
                            userId = userId,
                            orderAmount = 15000,
                            orderId = "unique-order-id-$index"
                        )
                        redeemCouponService(command)
                        successCount.incrementAndGet()
                    } catch (e: UserCouponAlreadyRedeemedException) {
                        failureCount.incrementAndGet()
                    }
                }
            }
            jobs.joinAll()
        }

        // then
        successCount.get() shouldBe 1
        failureCount.get() shouldBe concurrentRequests - 1

        val foundUserCouponEntity = testUserCouponAccessor.findById(issuedCoupon.userCouponId)
        foundUserCouponEntity.shouldNotBeNull()
        foundUserCouponEntity.status shouldBe UserCouponStatus.REDEEMED

        couponTemplateStatsCache.getStats(couponTemplateId).second shouldBe 1L
    }
}