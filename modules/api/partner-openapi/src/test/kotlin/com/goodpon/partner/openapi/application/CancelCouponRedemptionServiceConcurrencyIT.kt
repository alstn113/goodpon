package com.goodpon.partner.openapi.application

import com.goodpon.domain.coupon.history.CouponActionType
import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.user.UserCouponStatus
import com.goodpon.partner.application.coupon.port.`in`.dto.CancelCouponRedemptionCommand
import com.goodpon.partner.application.coupon.port.`in`.dto.IssueCouponCommand
import com.goodpon.partner.application.coupon.port.`in`.dto.RedeemCouponCommand
import com.goodpon.partner.application.coupon.service.CancelCouponRedemptionService
import com.goodpon.partner.application.coupon.service.IssueCouponService
import com.goodpon.partner.application.coupon.service.RedeemCouponService
import com.goodpon.partner.application.coupon.service.exception.UserCouponAlreadyCanceledException
import com.goodpon.partner.openapi.support.AbstractIntegrationTest
import com.goodpon.partner.openapi.support.accessor.TestCouponHistoryAccessor
import com.goodpon.partner.openapi.support.accessor.TestCouponTemplateAccessor
import com.goodpon.partner.openapi.support.accessor.TestCouponTemplateStatsAccessor
import com.goodpon.partner.openapi.support.accessor.TestUserCouponAccessor
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
    private val testUserCouponAccessor: TestUserCouponAccessor,
    private val testCouponTemplateAccessor: TestCouponTemplateAccessor,
    private val testCouponHistoryAccessor: TestCouponHistoryAccessor,
    private val testCouponTemplateStatsAccessor: TestCouponTemplateStatsAccessor,
) : AbstractIntegrationTest() {

    @Test
    fun `동시에 쿠폰 사용 취소 요청을 여러번 보내도 하나의 취소만 처리된다`(): Unit = runBlocking {
        // given
        val (merchantId: Long, couponTemplateId: Long) = testCouponTemplateAccessor.save(
            minOrderAmount = 10000,
            discountType = CouponDiscountType.FIXED_AMOUNT,
            discountValue = 2000
        )

        // - 쿠폰 발급
        val userId = "unique-user-id"
        val issueCouponCommand = IssueCouponCommand(
            userId = userId,
            couponTemplateId = couponTemplateId,
            merchantId = merchantId
        )
        val issueCouponResult = issueCouponService(issueCouponCommand)

        // - 쿠폰 사용
        val orderId = "unique-order-id"
        val orderAmount = 15000
        val redeemCouponCommand = RedeemCouponCommand(
            merchantId = merchantId,
            userCouponId = issueCouponResult.userCouponId,
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

        val foundStatsEntity = testCouponTemplateStatsAccessor.findById(couponTemplateId)
        foundStatsEntity.shouldNotBeNull()
        foundStatsEntity.issueCount shouldBe 1
        foundStatsEntity.redeemCount shouldBe 0

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
