package com.goodpon.core.domain.coupon.template.vo

import com.goodpon.core.support.error.CoreException
import com.goodpon.core.support.error.ErrorType
import java.time.LocalDate
import java.time.LocalDateTime

data class CouponPeriod(
    val issueStartAt: LocalDateTime,
    val issueEndAt: LocalDateTime? = null,
    val validityDays: Int? = null,
    val absoluteExpiresAt: LocalDateTime? = null,
) {
    init {
        validateDateRanges(issueStartAt, issueEndAt, absoluteExpiresAt)
        validateValidityDays(validityDays)
    }

    fun isIssuable(issueAt: LocalDateTime): Boolean {
        val started = issueStartAt <= issueAt
        val notEnded = issueEndAt == null || issueAt < issueEndAt
        return started && notEnded
    }

    fun calculateExpiresAt(issueDate: LocalDate): LocalDateTime? {
        val calculatedValidityEndAt = validityDays?.let {
            issueDate.plusDays(it.toLong()).plusDays(1).atStartOfDay()
        }

        return listOfNotNull(calculatedValidityEndAt, absoluteExpiresAt).minOrNull()
    }

    private fun validateDateRanges(
        issueStartAt: LocalDateTime,
        issueEndAt: LocalDateTime?,
        absoluteExpiresAt: LocalDateTime?,
    ) {
        if (issueEndAt != null && issueEndAt <= issueStartAt) {
            throw CoreException(ErrorType.COUPON_PERIOD_ISSUE_END_BEFORE_START)
        }
        if (absoluteExpiresAt != null && absoluteExpiresAt <= issueStartAt) {
            throw CoreException(ErrorType.COUPON_PERIOD_EXPIRE_BEFORE_START)
        }
        if (issueEndAt != null && absoluteExpiresAt != null && absoluteExpiresAt < issueEndAt) {
            throw CoreException(ErrorType.COUPON_PERIOD_EXPIRE_BEFORE_ISSUE_END)
        }
        if (absoluteExpiresAt != null && issueEndAt == null) {
            throw CoreException(ErrorType.COUPON_PERIOD_EXPIRE_WITHOUT_ISSUE_END)
        }
    }

    private fun validateValidityDays(validityDays: Int?) {
        if (validityDays != null && validityDays <= 0) {
            throw CoreException(ErrorType.COUPON_PERIOD_INVALID_VALIDITY_DAYS)
        }
    }
}