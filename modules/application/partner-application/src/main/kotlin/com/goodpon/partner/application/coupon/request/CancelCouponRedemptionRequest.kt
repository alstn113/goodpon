package com.goodpon.partner.application.coupon.request

data class CancelCouponRedemptionRequest(
    val couponId: String,
    val merchantId: Long,
    val cancelReason: String,
)
