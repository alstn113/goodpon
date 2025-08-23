package com.goodpon.api.partner.application.coupon

import com.goodpon.api.partner.support.AbstractIntegrationTest
import com.goodpon.api.partner.support.accessor.TestCouponHistoryAccessor
import com.goodpon.api.partner.support.accessor.TestCouponTemplateAccessor
import com.goodpon.api.partner.support.accessor.TestMerchantAccessor
import com.goodpon.api.partner.support.accessor.TestUserCouponAccessor
import com.goodpon.application.partner.coupon.port.`in`.dto.CancelCouponRedemptionCommand
import com.goodpon.application.partner.coupon.port.`in`.dto.IssueCouponCommand
import com.goodpon.application.partner.coupon.port.`in`.dto.RedeemCouponCommand
import com.goodpon.application.partner.coupon.port.out.CouponTemplateStatsCache
import com.goodpon.application.partner.coupon.service.CancelCouponRedemptionService
import com.goodpon.application.partner.coupon.service.IssueCouponService
import com.goodpon.application.partner.coupon.service.RedeemCouponService
import com.goodpon.application.partner.coupon.service.exception.UserCouponAlreadyCanceledException
import com.goodpon.domain.coupon.history.CouponActionType
import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.user.UserCouponStatus
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicInteger

class CancelCouponRedemptionServiceConcurrencyIT(
    private val cancelCouponRedemptionService: CancelCouponRedemptionService,
    private val issueCouponService: IssueCouponService,
    private val redeemCouponService: RedeemCouponService,
    private val testMerchantAccessor: TestMerchantAccessor,
    private val testUserCouponAccessor: TestUserCouponAccessor,
    private val testCouponTemplateAccessor: TestCouponTemplateAccessor,
    private val testCouponHistoryAccessor: TestCouponHistoryAccessor,
    private val couponTemplateStatsCache: CouponTemplateStatsCache,
) : AbstractIntegrationTest() {

    @Test
    fun `동시에 쿠폰 사용 취소 요청을 여러번 보내도 하나의 취소만 처리된다`(): Unit = runBlocking {
        // given
        val (merchantId) = testMerchantAccessor.createMerchant()
        val couponTemplateId = testCouponTemplateAccessor.createCouponTemplate(
            merchantId = merchantId,
            minOrderAmount = 10000,
            discountType = CouponDiscountType.FIXED_AMOUNT,
            discountValue = 2000
        )

        // - 쿠폰 발급
        val userId = "unique-user-id"
        issueCouponService(
            IssueCouponCommand(
                userId = userId,
                couponTemplateId = couponTemplateId,
                merchantId = merchantId
            )
        )
        val userCoupon = testUserCouponAccessor.issueCouponAndRecord(
            userId = userId,
            couponTemplateId = couponTemplateId,
            merchantId = merchantId
        )

        // - 쿠폰 사용
        val orderId = "unique-order-id"
        val orderAmount = 15000
        val redeemCouponCommand = RedeemCouponCommand(
            merchantId = merchantId,
            userCouponId = userCoupon.id,
            userId = userId,
            orderAmount = orderAmount,
            orderId = orderId
        )
        val redeemCouponResult = redeemCouponService(redeemCouponCommand)

        // - 동시성 테스트를 위한 값 설정
        val concurrentRequests = 5

        val successCount = AtomicInteger(0)
        val failureCount = AtomicInteger(0)

        coroutineScope {
            val jobs = List(concurrentRequests) {
                launch {
                    try {
                        val cancelCouponRedemptionCommand = CancelCouponRedemptionCommand(
                            merchantId = merchantId,
                            userCouponId = redeemCouponResult.userCouponId,
                            orderId = orderId,
                            cancelReason = "결제 실패"
                        )
                        cancelCouponRedemptionService(cancelCouponRedemptionCommand)
                        successCount.incrementAndGet()
                    } catch (e: UserCouponAlreadyCanceledException) {
                        failureCount.incrementAndGet()
                    }
                }
            }
            jobs.forEach { it.join() }
        }

        // then
        successCount.get() shouldBe 1
        failureCount.get() shouldBe concurrentRequests - 1

        val foundUserCouponEntity = testUserCouponAccessor.findById(redeemCouponResult.userCouponId)
        foundUserCouponEntity.shouldNotBeNull()
        foundUserCouponEntity.status shouldBe UserCouponStatus.ISSUED

        couponTemplateStatsCache.getStats(couponTemplateId).let {
            it.first shouldBe 1L
            it.second shouldBe 0L
        }

        val foundCouponHistoryEntities = testCouponHistoryAccessor.findByUserCouponId(redeemCouponResult.userCouponId)
        foundCouponHistoryEntities.shouldNotBeNull()
        foundCouponHistoryEntities.size shouldBe 3
        foundCouponHistoryEntities.sortedBy { it.createdAt }.also { sortedHistory ->
            sortedHistory[0].actionType shouldBe CouponActionType.ISSUE
            sortedHistory[1].actionType shouldBe CouponActionType.REDEEM
            sortedHistory[2].actionType shouldBe CouponActionType.CANCEL_REDEMPTION
        }
    }
}
