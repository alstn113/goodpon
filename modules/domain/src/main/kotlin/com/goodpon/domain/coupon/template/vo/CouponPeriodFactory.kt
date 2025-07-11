package com.goodpon.domain.coupon.template.vo

import com.goodpon.domain.coupon.template.exception.creation.*
import java.time.LocalDate

object CouponPeriodFactory {

    fun create(
        issueStartDate: LocalDate,
        issueEndDate: LocalDate? = null,
        validityDays: Int? = null,
        absoluteExpiryDate: LocalDate? = null,
    ): Result<CouponPeriod> {
        val validationResult = validate(
            issueStartDate = issueStartDate,
            issueEndDate = issueEndDate,
            validityDays = validityDays,
            absoluteExpiryDate = absoluteExpiryDate
        )
        validationResult.onFailure { return Result.failure(it) }

        val issueStartAt = issueStartDate.atStartOfDay()
        val issueEndAt = issueEndDate?.plusDays(1)?.atStartOfDay()
        val absoluteExpiresAt = absoluteExpiryDate?.plusDays(1)?.atStartOfDay()

        val couponPeriod = CouponPeriod(
            issueStartAt = issueStartAt,
            issueEndAt = issueEndAt,
            validityDays = validityDays,
            absoluteExpiresAt = absoluteExpiresAt
        )
        return Result.success(couponPeriod)
    }

    private fun validate(
        issueStartDate: LocalDate,
        issueEndDate: LocalDate?,
        validityDays: Int?,
        absoluteExpiryDate: LocalDate?,
    ): Result<Unit> {
        if (issueEndDate != null && issueEndDate < issueStartDate) {
            return Result.failure(CouponPeriodInvalidIssueEndBeforeStartException(issueEndDate = issueEndDate))
        }
        if (absoluteExpiryDate != null && absoluteExpiryDate < issueStartDate) {
            return Result.failure(CouponPeriodInvalidExpireBeforeStartException(absoluteExpiryDate = absoluteExpiryDate))
        }
        if (issueEndDate != null && absoluteExpiryDate != null && absoluteExpiryDate < issueEndDate) {
            return Result.failure(CouponPeriodInvalidExpireBeforeIssueEndException(absoluteExpiryDate = absoluteExpiryDate))
        }
        if (absoluteExpiryDate != null && issueEndDate == null) {
            return Result.failure(CouponPeriodInvalidExpireWithoutIssueEndException())
        }
        if (validityDays != null && validityDays <= 0) {
            return Result.failure(CouponPeriodInvalidValidityDaysException(validityDays = validityDays))
        }
        return Result.success(Unit)
    }
}