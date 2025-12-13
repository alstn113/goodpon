package com.goodpon.application.couponissuer.service.listener

data class CouponIssuedEvent(
    val couponTemplateId: Long,
    val userId: String,
)