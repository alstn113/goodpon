package com.goodpon.domain.coupon.service

import com.goodpon.domain.coupon.user.UserCoupon

data class CouponRedemptionResult(
    val redeemedCoupon: UserCoupon,
    val originalPrice: Int,
    val discountAmount: Int,
    val finalPrice: Int,
)