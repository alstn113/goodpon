package com.goodpon.couponissuer.application.service.listener

data class CouponIssuedEvent(
    val couponTemplateId: Long,
    val userId: String,
)