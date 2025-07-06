package com.goodpon.domain.coupon.template.vo

import com.goodpon.domain.coupon.template.exception.CouponPeriodInvalidExpireBeforeIssueEndException
import com.goodpon.domain.coupon.template.exception.CouponPeriodInvalidExpireBeforeStartException
import com.goodpon.domain.coupon.template.exception.CouponPeriodInvalidIssueEndBeforeStartException
import com.goodpon.domain.coupon.template.exception.CouponPeriodInvalidValidityDaysException
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
            throw CouponPeriodInvalidIssueEndBeforeStartException()
        }
        if (absoluteExpiresAt != null && absoluteExpiresAt <= issueStartAt) {
            throw CouponPeriodInvalidExpireBeforeStartException()
        }
        if (issueEndAt != null && absoluteExpiresAt != null && absoluteExpiresAt < issueEndAt) {
            throw CouponPeriodInvalidExpireBeforeIssueEndException()
        }
        if (absoluteExpiresAt != null && issueEndAt == null) {
            throw com.goodpon.domain.coupon.template.exception.CouponPeriodInvalidExpireWithoutIssueEndException()
        }
    }

    private fun validateValidityDays(validityDays: Int?) {
        if (validityDays != null && validityDays <= 0) {
            throw CouponPeriodInvalidValidityDaysException()
        }
    }
}