package com.goodpon.core.application.coupon

import com.goodpon.core.application.coupon.accessor.CouponHistoryReader
import com.goodpon.core.application.coupon.accessor.CouponHistoryStore
import com.goodpon.core.application.coupon.accessor.UserCouponStore
import com.goodpon.core.application.coupon.response.CouponCancelRedemptionResultResponse
import com.goodpon.core.domain.coupon.user.UserCoupon
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
        userCoupon: UserCoupon,
        reason: String,
        cancelAt: LocalDateTime,
    ): CouponCancelRedemptionResultResponse {
        val lastRedeemHistory = couponHistoryReader.readLastRedeemHistory(userCoupon.id)

        val canceledCoupon = userCouponStore.update(userCoupon.cancelRedemption())
        couponHistoryStore.recordCancelRedemption(
            userCouponId = canceledCoupon.id,
            orderId = lastRedeemHistory.orderId!!,
            reason = reason,
            recordedAt = cancelAt
        )

        if (canceledCoupon.hasExpired(cancelAt)) {
            val expiredCoupon = userCouponStore.update(canceledCoupon.expire())
            couponHistoryStore.recordExpired(
                userCouponId = expiredCoupon.id,
                recordedAt = cancelAt
            )
        }

        return CouponCancelRedemptionResultResponse(
            userCouponId = canceledCoupon.id,
            status = canceledCoupon.status,
            canceledAt = cancelAt,
            cancelReason = reason,
        )
    }
}