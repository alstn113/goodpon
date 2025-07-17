package com.goodpon.partner.openapi.application

import com.goodpon.domain.coupon.template.exception.CouponTemplateIssuanceLimitExceededException
import com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType
import com.goodpon.partner.application.coupon.port.`in`.dto.IssueCouponCommand
import com.goodpon.partner.application.coupon.service.IssueCouponService
import com.goodpon.partner.application.coupon.service.exception.UserCouponAlreadyIssuedException
import com.goodpon.partner.openapi.support.AbstractIntegrationTest
import com.goodpon.partner.openapi.support.accessor.*
import io.kotest.common.runBlocking
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicInteger

class IssueCouponServiceConcurrencyIT(
    private val issueCouponService: IssueCouponService,
    private val testMerchantAccessor: TestMerchantAccessor,
    private val testCouponTemplateAccessor: TestCouponTemplateAccessor,
    private val testUserCouponAccessor: TestUserCouponAccessor,
    private val testCouponTemplateStatsAccessor: TestCouponTemplateStatsAccessor,
    private val testCouponHistoryAccessor: TestCouponHistoryAccessor,
) : AbstractIntegrationTest() {

    @Test
    fun `여러 사용자가 동시에 쿠폰 발급 시 발급 제한을 초과하지 않는다`(): Unit = runBlocking {
        val maxIssueCount = 10L
        val (merchantId) = testMerchantAccessor.createMerchant()
        val couponTemplateId = testCouponTemplateAccessor.createCouponTemplate(
            merchantId = merchantId,
            limitType = CouponLimitPolicyType.ISSUE_COUNT,
            maxIssueCount = maxIssueCount,
            maxRedeemCount = null,
        )

        val userCounts = 20

        val successCount = AtomicInteger(0)
        val failureCount = AtomicInteger(0)

        coroutineScope {
            val jobs = List(userCounts) { userIndex ->
                launch(Dispatchers.IO) {
                    try {
                        val command = IssueCouponCommand(
                            merchantId = merchantId,
                            couponTemplateId = couponTemplateId,
                            userId = "user-$userIndex"
                        )
                        issueCouponService(command)
                        successCount.incrementAndGet()
                    } catch (e: CouponTemplateIssuanceLimitExceededException) {
                        failureCount.incrementAndGet()
                    }
                }
            }
            jobs.joinAll()
        }

        successCount.get() shouldBe maxIssueCount
        failureCount.get() shouldBe userCounts - maxIssueCount

        val foundUserCouponEntities = testUserCouponAccessor.findByCouponTemplateId(couponTemplateId)
        foundUserCouponEntities.size shouldBe maxIssueCount

        val foundStatsEntity = testCouponTemplateStatsAccessor.findById(couponTemplateId)
        foundStatsEntity.shouldNotBeNull()
        foundStatsEntity.issueCount shouldBe maxIssueCount

        val foundHistoryEntities = testCouponHistoryAccessor.findAll()
        foundHistoryEntities.size shouldBe maxIssueCount
    }

    @Test
    fun `동일한 사용자가 동일한 쿠폰을 동시에 발급 시 하나만 발급된다`(): Unit = runBlocking {
        val (merchantId) = testMerchantAccessor.createMerchant()
        val couponTemplateId = testCouponTemplateAccessor.createCouponTemplate(merchantId = merchantId)
        val userId = "user-unique"
        val concurrentRequests = 5

        val successCount = AtomicInteger(0)
        val failureCount = AtomicInteger(0)

        coroutineScope {
            val jobs = List(concurrentRequests) { _ ->
                launch(Dispatchers.IO) {
                    try {
                        val command = IssueCouponCommand(
                            merchantId = merchantId,
                            couponTemplateId = couponTemplateId,
                            userId = userId
                        )
                        issueCouponService(command)
                        successCount.incrementAndGet()
                    } catch (e: UserCouponAlreadyIssuedException) {
                        failureCount.incrementAndGet()
                    }
                }
            }
            jobs.joinAll()
        }

        successCount.get() shouldBe 1
        failureCount.get() shouldBe concurrentRequests - 1

        val foundUserCouponEntities = testUserCouponAccessor.findByCouponTemplateId(couponTemplateId)
        foundUserCouponEntities.size shouldBe 1
        foundUserCouponEntities.first().userId shouldBe userId

        val foundStatsEntity = testCouponTemplateStatsAccessor.findById(couponTemplateId)
        foundStatsEntity.shouldNotBeNull()
        foundStatsEntity.issueCount shouldBe 1

        val foundHistoryEntities = testCouponHistoryAccessor.findAll()
        foundHistoryEntities.size shouldBe 1
    }
}
