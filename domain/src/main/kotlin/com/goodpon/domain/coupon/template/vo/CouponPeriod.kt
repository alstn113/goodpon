package com.goodpon.domain.coupon.template.vo

import java.time.LocalDate
import java.time.LocalDateTime

data class CouponPeriod(
    val issueStartAt: LocalDateTime,
    val issueEndAt: LocalDateTime? = null,
    val validityDays: Int? = null,
    val absoluteExpiresAt: LocalDateTime? = null,
) {

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
}