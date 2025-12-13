package com.goodpon.application.dashboard.coupon.port.out.dto

data class IssueCouponRequestedEvent(
    val couponTemplateId: Long,
    val userId: String,
)
