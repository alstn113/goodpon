package com.goodpon.partner.application.coupon.port.`in`.dto

import java.time.LocalDateTime

data class IssueCouponResult(
    val userCouponId: String,
    val userId: String,
    val couponTemplateId: Long,
    val couponTemplateName: String,
    val issuedAt: LocalDateTime,
    val expiresAt: LocalDateTime?,
)
