package com.goodpon.partner.application.coupon.service.request

data class IssueCouponRequest(
    val merchantId: Long,
    val couponTemplateId: Long,
    val userId: String,
)