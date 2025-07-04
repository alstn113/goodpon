package com.goodpon.core.application.coupon

import com.goodpon.core.application.coupon.accessor.UserCouponStore
import com.goodpon.core.application.coupon.response.CouponRedemptionResultResponse
import com.goodpon.core.domain.coupon.template.CouponTemplate
import com.goodpon.core.domain.coupon.user.UserCoupon
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class CouponRedeemer(
    private val couponHistoryRecorder: CouponHistoryRecorder,
    private val userCouponStore: UserCouponStore,
) {
    @Transactional
    fun redeemCoupon(
        couponTemplate: CouponTemplate,
        userCoupon: UserCoupon,
        redeemCount: Long,
        orderAmount: Int,
        orderId: String,
        redeemAt: LocalDateTime,
    ): CouponRedemptionResultResponse {
        couponTemplate.validateRedeem(currentRedeemedCount = redeemCount, orderAmount = orderAmount)

        val discountAmount = couponTemplate.calculateDiscountAmount(orderAmount)
        val finalPrice = couponTemplate.calculateFinalPrice(orderAmount)

        val redeemedCoupon = userCouponStore.update(userCoupon.redeem(redeemAt = redeemAt))
        couponHistoryRecorder.recordRedeemed(
            userCouponId = redeemedCoupon.id,
            orderId = orderId,
            recordedAt = redeemAt
        )

        return CouponRedemptionResultResponse(
            couponId = userCoupon.id,
            discountAmount = discountAmount,
            originalPrice = orderAmount,
            finalPrice = finalPrice,
            orderId = orderId,
            redeemedAt = redeemAt
        )
    }
}
