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

// (발급 시작 기간 + 유효 기간) < 사용 종료 기간 => 사용 종료 기간까지 사용 가능한걸로 생각 -> IssuedCoupon
// 참고로 시간은 start <= x < end ex) 00:00:00 <= x < 23:59:59
