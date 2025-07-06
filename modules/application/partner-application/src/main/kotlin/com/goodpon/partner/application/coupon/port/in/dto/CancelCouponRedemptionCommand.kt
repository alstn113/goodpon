package com.goodpon.partner.application.coupon.port.`in`.dto

data class CancelCouponRedemptionCommand(
    val couponId: String,
    val merchantId: Long,
    val cancelReason: String,
)