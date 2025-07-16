package com.goodpon.partner.application.coupon.service

import com.goodpon.domain.coupon.history.CouponActionType
import com.goodpon.domain.coupon.user.UserCoupon
import com.goodpon.partner.application.coupon.service.accessor.CouponHistoryAccessor
import com.goodpon.partner.application.coupon.service.accessor.UserCouponAccessor
import com.goodpon.partner.application.coupon.service.exception.CouponHistoryLastActionTypeNotRedeemException
import com.goodpon.partner.application.coupon.service.exception.CouponOrderIdMismatchException
import com.goodpon.partner.application.coupon.service.exception.UserCouponAlreadyCanceledException
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
        if (!userCoupon.isRedeemed()) {
            throw UserCouponAlreadyCanceledException()
        }

        val lastHistory = couponHistoryAccessor.findLastCouponHistory(userCoupon.id)
            ?.takeIf { it.actionType == CouponActionType.REDEEM }
            ?: throw CouponHistoryLastActionTypeNotRedeemException()

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