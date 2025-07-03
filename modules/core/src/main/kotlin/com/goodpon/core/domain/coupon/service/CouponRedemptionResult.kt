package com.goodpon.core.domain.coupon.service

import java.time.LocalDateTime

data class CouponRedemptionResult(
    val couponId: String,
    val discountAmount: Int,
    val originalPrice: Int,
    val finalPrice: Int,
    val orderId: String,
    val redeemedAt: LocalDateTime,
)
