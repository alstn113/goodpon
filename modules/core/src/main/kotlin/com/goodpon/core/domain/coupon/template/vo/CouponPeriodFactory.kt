package com.goodpon.core.domain.coupon.template.vo

import java.time.LocalDate

object CouponPeriodFactory {

    fun create(
        issueStartDate: LocalDate,
        issueEndDate: LocalDate? = null,
        validityDays: Int? = null,
        absoluteExpiryDate: LocalDate? = null,
    ): CouponPeriod {
        val issueStartAt = issueStartDate.atStartOfDay()
        val issueEndAt = issueEndDate?.plusDays(1)?.atStartOfDay()
        val absoluteExpiresAt = absoluteExpiryDate?.plusDays(1)?.atStartOfDay()

        return CouponPeriod(
            issueStartAt = issueStartAt,
            issueEndAt = issueEndAt,
            validityDays = validityDays,
            absoluteExpiresAt = absoluteExpiresAt
        )
    }
}