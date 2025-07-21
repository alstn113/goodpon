package com.goodpon.application.partner.coupon

import com.goodpon.application.partner.coupon.service.CouponRedemptionCancelProcessor
import com.goodpon.application.partner.coupon.service.accessor.CouponHistoryAccessor
import com.goodpon.application.partner.coupon.service.accessor.UserCouponAccessor
import com.goodpon.application.partner.coupon.service.exception.CouponOrderIdMismatchException
import com.goodpon.application.partner.coupon.service.exception.UserCouponAlreadyCanceledException
import com.goodpon.application.partner.coupon.service.exception.UserCouponCancelRedemptionNotAllowedException
import com.goodpon.domain.coupon.history.CouponActionType
import com.goodpon.domain.coupon.history.CouponHistory
import com.goodpon.domain.coupon.user.UserCoupon
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime

class CouponRedemptionCancelProcessorTest : DescribeSpec({

    val couponHistoryAccessor = mockk<CouponHistoryAccessor>()
    val userCouponAccessor = mockk<UserCouponAccessor>()

    val couponRedemptionCancelProcessor = CouponRedemptionCancelProcessor(
        couponHistoryAccessor = couponHistoryAccessor,
        userCouponAccessor = userCouponAccessor
    )

    val userCouponId = "test-user-coupon-id"
    val orderId = "test-order-id"
    val cancelReason = "결제 취소"
    val cancelAt = LocalDateTime.now()

    describe("process 메서드") {
        val userCoupon = mockk<UserCoupon>()
        val lastHistory = mockk<CouponHistory>()

        beforeTest {
            every { userCoupon.id } returns userCouponId
            every { couponHistoryAccessor.findLastCouponHistory(any()) } returns lastHistory
            every { lastHistory.actionType } returns CouponActionType.REDEEM
            every { userCoupon.isRedeemed() } returns true
            every { lastHistory.orderId } returns orderId
        }

        it("쿠폰 내역이 존재하지 않으면 예외를 던진다") {
            every { couponHistoryAccessor.findLastCouponHistory(any()) } returns null

            shouldThrow<IllegalStateException> {
                couponRedemptionCancelProcessor.process(userCoupon, orderId, cancelReason, cancelAt)
            }
        }

        it("이미 취소된 쿠폰인 경우 예외를 던진다") {
            every { lastHistory.actionType } returns CouponActionType.CANCEL_REDEMPTION

            shouldThrow<UserCouponAlreadyCanceledException> {
                couponRedemptionCancelProcessor.process(userCoupon, orderId, cancelReason, cancelAt)
            }
        }

        it("쿠폰이 '사용됨' 상태가 아닌 경우 예외를 던진다") {
            every { userCoupon.isRedeemed() } returns false

            shouldThrow<UserCouponCancelRedemptionNotAllowedException> {
                couponRedemptionCancelProcessor.process(userCoupon, orderId, cancelReason, cancelAt)
            }
        }

        it("마지막 쿠폰 내역의 타입이 '사용됨'이 아닌 경우 예외를 던진다") {
            every { lastHistory.actionType } returns CouponActionType.ISSUE

            shouldThrow<UserCouponCancelRedemptionNotAllowedException> {
                couponRedemptionCancelProcessor.process(userCoupon, orderId, cancelReason, cancelAt)
            }
        }

        it("쿠폰 사용 시 주문 ID가 쿠폰 취소의 주문 ID와 일치하지 않는 경우 예외를 던진다") {
            val lastOrderId = "last-order-id"
            val wrongOrderId = "wrong-order-id"
            every { lastHistory.orderId } returns lastOrderId

            shouldThrow<CouponOrderIdMismatchException> {
                couponRedemptionCancelProcessor.process(userCoupon, wrongOrderId, cancelReason, cancelAt)
            }
        }
    }
})
