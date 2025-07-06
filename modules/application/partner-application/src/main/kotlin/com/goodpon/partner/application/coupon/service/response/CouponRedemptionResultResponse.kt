package com.goodpon.partner.application.coupon.service.response

import java.time.LocalDateTime

data class CouponRedemptionResultResponse(
    val couponId: String,
    val discountAmount: Int,
    val originalPrice: Int,
    val finalPrice: Int,
    val orderId: String,
    val redeemedAt: LocalDateTime,
)
