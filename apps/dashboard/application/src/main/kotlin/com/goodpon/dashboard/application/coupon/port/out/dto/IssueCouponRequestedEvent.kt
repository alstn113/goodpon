package com.goodpon.dashboard.application.coupon.port.out.dto

data class IssueCouponRequestedEvent(
    val couponTemplateId: Long,
    val userId: String,
)
