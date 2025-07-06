package com.goodpon.partner.application.coupon.response

import java.time.LocalDateTime

data class CouponIssueResultResponse(
    val userCouponId: String,
    val userId: String,
    val couponTemplateId: Long,
    val couponTemplateName: String,
    val issuedAt: LocalDateTime,
    val expiresAt: LocalDateTime?,
)
