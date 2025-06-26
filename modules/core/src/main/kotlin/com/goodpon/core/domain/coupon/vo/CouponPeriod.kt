package com.goodpon.core.domain.coupon.vo

import java.time.LocalDate
import java.time.LocalDateTime

data class CouponPeriod(
    val issueStartAt: LocalDateTime,
    val issueEndAt: LocalDateTime? = null,
    val validityDays: Int? = null,
    val redeemEndAt: LocalDateTime? = null,
) {

    init {
        validateDateRanges(issueStartAt, issueEndAt, redeemEndAt)
        validateValidityDays(validityDays)
    }

    fun isIssuable(now: LocalDateTime = LocalDateTime.now()): Boolean {
        val started = issueStartAt <= now
        val notEnded = issueEndAt == null || now < issueEndAt
        return started && notEnded
    }

    fun calculateFinalUsageEndAt(issueDate: LocalDate): LocalDateTime? {
        val validityEndAt = validityDays?.let {
            issueDate.plusDays(it + 1L).atStartOfDay()
        }

        return when {
            validityEndAt != null && redeemEndAt != null -> minOf(validityEndAt, redeemEndAt)
            validityEndAt != null -> validityEndAt
            redeemEndAt != null -> redeemEndAt
            else -> null
        }
    }

    private fun validateDateRanges(
        issueStartAt: LocalDateTime,
        issueEndAt: LocalDateTime?,
        redeemEndAt: LocalDateTime?,
    ) {
        if (issueEndAt != null && issueEndAt <= issueStartAt) {
            throw IllegalArgumentException("발급 종료 기간은 발급 시작 기간 이후여야 합니다.")
        }

        if (redeemEndAt != null && redeemEndAt <= issueStartAt) {
            throw IllegalArgumentException("사용 종료 기간은 발급 시작 기간 이후여야 합니다.")
        }

        if (issueEndAt != null && redeemEndAt != null && redeemEndAt < issueEndAt) {
            throw IllegalArgumentException("사용 종료 기간은 발급 종료 기간 이후이거나 같아야 합니다.")
        }

        if (issueEndAt == null && redeemEndAt != null) {
            throw IllegalArgumentException("발급 종료 기간이 없으면 사용 종료 기간을 설정할 수 없습니다.")
        }
    }

    private fun validateValidityDays(validityDays: Int?) {
        if (validityDays != null && validityDays <= 0) {
            throw IllegalArgumentException("유효 기간은 0보다 커야 합니다.")
        }
    }
}