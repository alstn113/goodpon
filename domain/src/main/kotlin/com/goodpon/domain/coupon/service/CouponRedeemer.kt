package com.goodpon.domain.coupon.service

import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.user.UserCoupon
import java.time.LocalDateTime

object CouponRedeemer {

    fun redeem(
        couponTemplate: CouponTemplate,
        userCoupon: UserCoupon,
        orderAmount: Int,
        redeemAt: LocalDateTime,
    ): CouponRedemptionResult {
        couponTemplate.validateRedeem(orderAmount = orderAmount)

        val redeemedCoupon = userCoupon.redeem(redeemAt = redeemAt)

        val discountAmount = couponTemplate.calculateDiscountAmount(orderAmount)
        val finalPrice = orderAmount - discountAmount

        return CouponRedemptionResult(
            redeemedCoupon = redeemedCoupon,
            originalPrice = orderAmount,
            discountAmount = discountAmount,
            finalPrice = finalPrice,
        )
    }
}