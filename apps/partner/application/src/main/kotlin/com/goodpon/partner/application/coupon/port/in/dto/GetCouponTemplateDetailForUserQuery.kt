package com.goodpon.partner.application.coupon.port.`in`.dto

data class GetCouponTemplateDetailForUserQuery(
    val merchantId: Long,
    val couponTemplateId: Long,
    val userId: String? = null,
)