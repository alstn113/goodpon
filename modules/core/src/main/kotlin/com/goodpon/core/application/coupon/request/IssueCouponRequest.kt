package com.goodpon.core.application.coupon.request

data class IssueCouponRequest(
    val merchantId: Long,
    val couponTemplateId: Long,
    val userId: String,
)