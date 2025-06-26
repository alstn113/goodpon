package com.goodpon.core.domain.coupon

import java.time.LocalDateTime

data class CouponRedemptionResult(
    val couponId: String,
    val discountAmount: Int,
    val originalPrice: Int,
    val finalPrice: Int,
    val redeemedAt: LocalDateTime
)
