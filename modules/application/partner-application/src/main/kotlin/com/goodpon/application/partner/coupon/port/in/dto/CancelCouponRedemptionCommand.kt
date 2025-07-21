package com.goodpon.application.partner.coupon.port.`in`.dto

data class CancelCouponRedemptionCommand(
    val userCouponId: String,
    val orderId: String,
    val merchantId: Long,
    val cancelReason: String,
)