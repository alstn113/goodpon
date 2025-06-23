package com.goodpon.core.domain.coupon

import com.goodpon.core.domain.coupon.vo.*
import java.time.LocalDate
import java.time.LocalDateTime

object CouponTemplateFactory {

    fun create(
        merchantId: Long,
        name: String,
        description: String,
        minOrderAmount: Long? = null,
        discountType: CouponDiscountType,
        discountValue: Int,
        maxDiscountAmount: Int? = null,
        issueStartDate: LocalDate,
        issueEndDate: LocalDate?,
        validityDays: Int?,
        usageEndDate: LocalDate?,
        limitType: CouponLimitType,
        maxIssueLimit: Long? = null,
        maxUsageLimit: Long? = null,
    ): CouponTemplate {
        val usageCondition = CouponUsageCondition(minOrderAmount = minOrderAmount)
        val discountPolicy = CouponDiscountPolicy(
            discountType = discountType,
            discountValue = discountValue,
            maxDiscountAmount = maxDiscountAmount
        )
        val period = CouponPeriodFactory.create(
            issueStartDate = issueStartDate,
            issueEndDate = issueEndDate,
            validityDays = validityDays,
            usageEndDate = usageEndDate
        )
        val limitPolicy = CouponLimitPolicy(
            limitType = limitType,
            maxIssueLimit = maxIssueLimit,
            maxUsageLimit = maxUsageLimit
        )

        return CouponTemplate(
            id = 0,
            merchantId = merchantId,
            name = name,
            description = description,
            usageCondition = usageCondition,
            discountPolicy = discountPolicy,
            period = period,
            limitPolicy = limitPolicy,
            status = CouponTemplateStatus.DRAFT,
            isIssuable = true,
            isUsable = true
        )
    }

    fun reconstitute(
        id: Long,
        merchantId: Long,
        name: String,
        description: String,
        minOrderAmount: Long? = null,
        discountType: CouponDiscountType,
        discountValue: Int,
        maxDiscountAmount: Int? = null,
        issueStartAt: LocalDateTime,
        issueEndAt: LocalDateTime?,
        validityDays: Int?,
        usageEndAt: LocalDateTime?,
        limitType: CouponLimitType,
        maxIssueLimit: Long? = null,
        maxUsageLimit: Long? = null,
        status: CouponTemplateStatus,
        isIssuable: Boolean,
        isUsable: Boolean,
    ): CouponTemplate {
        return CouponTemplate(
            id = id,
            merchantId = merchantId,
            name = name,
            description = description,
            usageCondition = CouponUsageCondition(minOrderAmount),
            discountPolicy = CouponDiscountPolicy(discountType, discountValue, maxDiscountAmount),
            period = CouponPeriod(issueStartAt, issueEndAt, validityDays, usageEndAt),
            limitPolicy = CouponLimitPolicy(limitType, maxIssueLimit, maxUsageLimit),
            status = status,
            isIssuable = isIssuable,
            isUsable = isUsable
        )
    }
}