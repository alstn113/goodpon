package com.goodpon.domain.coupon.service

import com.goodpon.domain.coupon.user.UserCoupon

data class CouponRedemptionResult(
    val redeemedCoupon: com.goodpon.domain.coupon.user.UserCoupon,
    val originalPrice: Int,
    val discountAmount: Int,
    val finalPrice: Int,
)