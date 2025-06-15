package com.goodpon.core.domain.coupon

import java.time.LocalDate

object CouponPeriodFactory {

    fun create(
        issueStartDate: LocalDate,
        issueEndDate: LocalDate? = null,
        validityDays: Int? = null,
        useEndDate: LocalDate? = null,
    ): CouponPeriod {
        val issueStartAt = issueStartDate.atStartOfDay() // 발급 시작일은 자정부터 시작
        val issueEndAt = issueEndDate?.plusDays(1)?.atStartOfDay() // 종료일은 "포함하지 않는" 날짜의 자정으로 표현 (exclusive end)
        val useEndAt = useEndDate?.plusDays(1)?.atStartOfDay()

        return CouponPeriod.create(
            issueStartAt = issueStartAt,
            issueEndAt = issueEndAt,
            validityDays = validityDays,
            useEndAt = useEndAt
        )
    }
}