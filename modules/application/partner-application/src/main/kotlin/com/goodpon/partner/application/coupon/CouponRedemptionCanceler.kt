package com.goodpon.partner.application.coupon

import com.goodpon.partner.application.coupon.accessor.CouponHistoryReader
import com.goodpon.partner.application.coupon.accessor.CouponHistoryStore
import com.goodpon.partner.application.coupon.accessor.UserCouponStore
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class CouponRedemptionCanceler(
    val couponHistoryReader: CouponHistoryReader,
    val userCouponStore: UserCouponStore,
    val couponHistoryStore: CouponHistoryStore,
) {

    @Transactional
    fun cancelRedemption(
        userCoupon: com.goodpon.domain.coupon.user.UserCoupon,
        reason: String,
        cancelAt: LocalDateTime,
    ): com.goodpon.domain.coupon.user.UserCoupon {
        val lastRedeemHistory = couponHistoryReader.readLastRedeemHistory(userCoupon.id)

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
        userCoupon: com.goodpon.domain.coupon.user.UserCoupon,
        reason: String,
        orderId: String,
        cancelAt: LocalDateTime,
    ): com.goodpon.domain.coupon.user.UserCoupon {
        val canceledCoupon = userCoupon.cancelRedemption()
        val updatedCoupon = userCouponStore.update(canceledCoupon)

        couponHistoryStore.recordCancelRedemption(
            userCouponId = updatedCoupon.id,
            orderId = orderId,
            reason = reason,
            recordedAt = cancelAt
        )
        return updatedCoupon
    }

    private fun expireAndRecordIfExpired(
        userCoupon: com.goodpon.domain.coupon.user.UserCoupon,
        cancelAt: LocalDateTime,
    ): com.goodpon.domain.coupon.user.UserCoupon {
        if (userCoupon.hasExpired(cancelAt)) {
            val expiredCoupon = userCoupon.expire()
            userCouponStore.update(expiredCoupon)

            couponHistoryStore.recordExpired(
                userCouponId = expiredCoupon.id,
                recordedAt = cancelAt
            )
            return expiredCoupon
        }
        return userCoupon
    }
}