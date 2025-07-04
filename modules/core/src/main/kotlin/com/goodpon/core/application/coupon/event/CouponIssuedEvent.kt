package com.goodpon.core.application.coupon.event

import java.time.LocalDateTime

data class CouponIssuedEvent(
    val userCouponId: String,
    val couponTemplateId: Long,
    val userId: String,
    val issuedAt: LocalDateTime,
)