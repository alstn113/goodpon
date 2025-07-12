package com.goodpon.partner.application.coupon.service

import com.goodpon.domain.coupon.user.UserCoupon
import com.goodpon.partner.application.coupon.service.accessor.CouponHistoryAccessor
import com.goodpon.partner.application.coupon.service.accessor.UserCouponAccessor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CouponRedemptionCancelProcessor(
    val couponHistoryAccessor: CouponHistoryAccessor,
    val userCouponAccessor: UserCouponAccessor,
) {

    @Transactional
    fun process(userCoupon: UserCoupon, reason: String, cancelAt: LocalDateTime): UserCoupon {
        val lastRedeemHistory = couponHistoryAccessor.readLastRedeemHistory(userCoupon.id)

        val canceledCoupon = cancelAndRecord(
            userCoupon = userCoupon,
            reason = reason,
            orderId = lastRedeemHistory.orderId!!,
            cancelAt = cancelAt
        )

        expireAndRecordIfExpired(
            userCoupon = canceledCoupon,
            cancelAt = cancelAt
        )

        return canceledCoupon
    }

    private fun cancelAndRecord(
        userCoupon: UserCoupon,
        reason: String,
        orderId: String,
        cancelAt: LocalDateTime,
    ): UserCoupon {
        val canceledCoupon = userCoupon.cancelRedemption()
        val updatedCoupon = userCouponAccessor.update(canceledCoupon)

        couponHistoryAccessor.recordCancelRedemption(
            userCouponId = updatedCoupon.id,
            orderId = orderId,
            reason = reason,
            recordedAt = cancelAt
        )
        return updatedCoupon
    }

    private fun expireAndRecordIfExpired(userCoupon: UserCoupon, cancelAt: LocalDateTime): UserCoupon {
        if (userCoupon.hasExpired(cancelAt)) {
            val expiredCoupon = userCoupon.expire()
            userCouponAccessor.update(expiredCoupon)

            couponHistoryAccessor.recordExpired(
                userCouponId = expiredCoupon.id,
                recordedAt = cancelAt
            )
            return expiredCoupon
        }
        return userCoupon
    }
}