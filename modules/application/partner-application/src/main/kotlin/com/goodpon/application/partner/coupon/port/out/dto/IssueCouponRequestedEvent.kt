package com.goodpon.application.partner.coupon.port.out.dto

import java.time.LocalDateTime

data class IssueCouponRequestedEvent(
    val couponTemplateId: Long,
    val userId: String,
    val requestedAt: LocalDateTime,
)
