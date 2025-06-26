package com.goodpon.core.domain.coupon

import java.time.LocalDateTime

data class CouponIssueResult(
    val userCouponId: String,
    val userId: String,
    val couponTemplateId: Long,
    val couponTemplateName: String,
    val issuedAt: LocalDateTime,
    val expiresAt: LocalDateTime?,
)
