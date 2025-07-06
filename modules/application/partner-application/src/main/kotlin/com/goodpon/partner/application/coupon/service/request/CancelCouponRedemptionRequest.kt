package com.goodpon.partner.application.coupon.service.request

data class CancelCouponRedemptionRequest(
    val couponId: String,
    val merchantId: Long,
    val cancelReason: String,
)
