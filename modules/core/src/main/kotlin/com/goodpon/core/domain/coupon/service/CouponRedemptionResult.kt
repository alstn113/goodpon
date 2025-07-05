package com.goodpon.core.domain.coupon.service

import com.goodpon.core.domain.coupon.user.UserCoupon

data class CouponRedemptionResult(
    val redeemedCoupon: UserCoupon,
    val originalPrice: Int,
    val discountAmount: Int,
    val finalPrice: Int,
)