package com.goodpon.core.application.coupon.response

import java.time.LocalDateTime

data class CouponRedemptionResultResponse(
    val couponId: String,
    val discountAmount: Int,
    val originalPrice: Int,
    val finalPrice: Int,
    val orderId: String,
    val redeemedAt: LocalDateTime,
)
