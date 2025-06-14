package com.goodpon.core.domain.coupon

import java.time.LocalDate
import java.time.LocalDateTime

data class CouponPeriod private constructor(
    val issueStartAt: LocalDateTime,
    val issueEndAt: LocalDateTime?,
    val validityDays: Long?,
    val useEndAt: LocalDateTime?,
) {

    companion object {

        fun create(
            issueStartDate: LocalDate,
            issueEndDate: LocalDate? = null,
            validityDays: Long? = null,
            useEndDate: LocalDate? = null,
        ): CouponPeriod {
            val issueStartAt = issueStartDate.atStartOfDay() // 발급 시작일은 자정부터 시작
            val issueEndAt = issueEndDate?.plusDays(1)?.atStartOfDay() // 종료일은 "포함하지 않는" 날짜의 자정으로 표현 (exclusive end)
            val useEndAt = useEndDate?.plusDays(1)?.atStartOfDay()

            validateDateRanges(issueStartAt, issueEndAt, useEndAt)
            validateValidityDays(validityDays)

            return CouponPeriod(
                issueStartAt = issueStartAt,
                issueEndAt = issueEndAt,
                validityDays = validityDays,
                useEndAt = useEndAt
            )
        }

        private fun validateDateRanges(
            issueStartAt: LocalDateTime,
            issueEndAt: LocalDateTime?,
            useEndAt: LocalDateTime?,
        ) {
            if (issueEndAt != null && issueEndAt <= issueStartAt) {
                throw IllegalArgumentException("발급 종료 기간은 발급 시작 기간 이후여야 합니다.")
            }

            if (useEndAt != null && useEndAt <= issueStartAt) {
                throw IllegalArgumentException("사용 종료 기간은 발급 시작 기간 이후여야 합니다.")
            }

            if (issueEndAt != null && useEndAt != null && useEndAt < issueEndAt) {
                throw IllegalArgumentException("사용 종료 기간은 발급 종료 기간 이후이거나 같아야 합니다.")
            }

            if (issueEndAt == null && useEndAt != null) {
                throw IllegalArgumentException("발급 종료 기간이 없으면 사용 종료 기간을 설정할 수 없습니다.")
            }
        }

        private fun validateValidityDays(validityDays: Long?) {
            if (validityDays != null && validityDays <= 0) {
                throw IllegalArgumentException("유효 기간은 0보다 커야 합니다.")
            }
        }
    }

    fun isIssuable(now: LocalDateTime = LocalDateTime.now()): Boolean {
        val started = issueStartAt <= now
        val notEnded = issueEndAt == null || now < issueEndAt
        return started && notEnded
    }

    fun calculateFinalUseEndAt(issueDate: LocalDate): LocalDateTime? {
        val validityEndAt = validityDays?.let {
            issueDate.plusDays(it + 1).atStartOfDay()
        }

        return when {
            validityEndAt != null && useEndAt != null -> minOf(validityEndAt, useEndAt)
            validityEndAt != null -> validityEndAt
            useEndAt != null -> useEndAt
            else -> null
        }
    }
}
