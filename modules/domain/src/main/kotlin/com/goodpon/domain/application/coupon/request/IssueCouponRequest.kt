package com.goodpon.domain.application.coupon.request

data class IssueCouponRequest(
    val merchantId: Long,
    val couponTemplateId: Long,
    val userId: String,
)