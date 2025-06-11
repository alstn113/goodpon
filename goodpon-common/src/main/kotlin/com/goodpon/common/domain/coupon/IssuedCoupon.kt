package com.goodpon.common.domain.coupon

import java.time.LocalDateTime

data class IssuedCoupon(
    val id: Long,
    val couponTemplateId: Long,
    val accountId: String,
    val issuedAt: LocalDateTime,
    val expiresAt: LocalDateTime?,
    val isUsed: Boolean,
    val usedAt: LocalDateTime?,
    val updatedAt: LocalDateTime,
)