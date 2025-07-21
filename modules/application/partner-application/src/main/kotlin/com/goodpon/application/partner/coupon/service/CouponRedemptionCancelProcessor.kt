package com.goodpon.application.partner.coupon.service

import com.goodpon.application.partner.coupon.service.accessor.CouponHistoryAccessor
import com.goodpon.application.partner.coupon.service.accessor.UserCouponAccessor
import com.goodpon.application.partner.coupon.service.exception.CouponOrderIdMismatchException
import com.goodpon.application.partner.coupon.service.exception.UserCouponAlreadyCanceledException
import com.goodpon.application.partner.coupon.service.exception.UserCouponCancelRedemptionNotAllowedException
import com.goodpon.domain.coupon.history.CouponActionType
import com.goodpon.domain.coupon.user.UserCoupon
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CouponRedemptionCancelProcessor(
    private val couponHistoryAccessor: CouponHistoryAccessor,
    private val userCouponAccessor: UserCouponAccessor,
) {

    @Transactional
    fun process(userCoupon: UserCoupon, orderId: String, cancelReason: String, cancelAt: LocalDateTime): UserCoupon {
        validateRedeemHistory(userCoupon, orderId)

        val canceledCoupon = cancelAndRecord(
            userCoupon = userCoupon,
            cancelReason = cancelReason,
            orderId = orderId,
            cancelAt = cancelAt
        )

        return expireAndRecordIfExpired(canceledCoupon, cancelAt)
    }

    private fun validateRedeemHistory(userCoupon: UserCoupon, orderId: String) {
        val lastHistory = couponHistoryAccessor.findLastCouponHistory(userCoupon.id)
            ?: throw IllegalStateException("쿠폰은 존재하지만 쿠폰 내역이 존재하지 않습니다. userCouponId=${userCoupon.id}")

        if (lastHistory.actionType == CouponActionType.CANCEL_REDEMPTION) {
            throw UserCouponAlreadyCanceledException()
        }

        if (!userCoupon.isRedeemed() || lastHistory.actionType != CouponActionType.REDEEM) {
            throw UserCouponCancelRedemptionNotAllowedException()
        }

        if (lastHistory.orderId != orderId) {
            throw CouponOrderIdMismatchException()
        }
    }

    private fun cancelAndRecord(
        userCoupon: UserCoupon,
        cancelReason: String,
        orderId: String,
        cancelAt: LocalDateTime,
    ): UserCoupon {
        val canceledCoupon = userCouponAccessor.update(userCoupon.cancelRedemption())

        couponHistoryAccessor.recordCancelRedemption(
            userCouponId = canceledCoupon.id,
            orderId = orderId,
            reason = cancelReason,
            recordedAt = cancelAt
        )

        return canceledCoupon
    }

    private fun expireAndRecordIfExpired(userCoupon: UserCoupon, cancelAt: LocalDateTime): UserCoupon {
        if (!userCoupon.hasExpired(cancelAt)) {
            return userCoupon
        }

        val expiredCoupon = userCouponAccessor.update(userCoupon.expire())

        couponHistoryAccessor.recordExpired(
            userCouponId = expiredCoupon.id,
            recordedAt = cancelAt
        )

        return expiredCoupon
    }
}