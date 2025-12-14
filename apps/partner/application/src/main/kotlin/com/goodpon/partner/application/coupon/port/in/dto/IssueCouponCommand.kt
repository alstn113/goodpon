package com.goodpon.partner.application.coupon.port.`in`.dto

data class IssueCouponCommand(
    val merchantId: Long,
    val couponTemplateId: Long,
    val userId: String,
)