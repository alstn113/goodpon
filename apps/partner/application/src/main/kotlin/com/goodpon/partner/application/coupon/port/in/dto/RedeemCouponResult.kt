package com.goodpon.partner.application.coupon.port.`in`.dto

import java.time.LocalDateTime

data class RedeemCouponResult(
    val userCouponId: String,
    val discountAmount: Int,
    val originalPrice: Int,
    val finalPrice: Int,
    val orderId: String,
    val redeemedAt: LocalDateTime,
)
