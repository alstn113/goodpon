package com.goodpon.core.domain.coupon

import com.goodpon.core.domain.coupon.vo.*
import java.time.LocalDate
import java.time.LocalDateTime

object CouponTemplateFactory {

    fun create(
        merchantId: Long,
        name: String,
        description: String,
        minimumOrderAmount: Long? = null,
        discountType: DiscountType,
        discountValue: Int,
        issueStartDate: LocalDate,
        issueEndDate: LocalDate?,
        validityDays: Int?,
        useEndDateTime: LocalDate?,
        limitType: CouponTemplateLimitType,
        issueLimit: Long? = null,
        useLimit: Long? = null,
    ): CouponTemplate {
        val usageCondition = CouponUsageCondition(minimumOrderAmount)
        val discountPolicy = DiscountPolicy(
            discountType = discountType,
            discountValue = discountValue
        )
        val couponPeriod = CouponPeriodFactory.create(
            issueStartDate = issueStartDate,
            issueEndDate = issueEndDate,
            validityDays = validityDays,
            useEndDate = useEndDateTime
        )
        val usageLimitPolicy = UsageLimitPolicy(
            limitType = limitType,
            issueLimit = issueLimit,
            useLimit = useLimit
        )

        return CouponTemplate(
            id = 0,
            merchantId = merchantId,
            name = name,
            description = description,
            usageCondition = usageCondition,
            discountPolicy = discountPolicy,
            couponPeriod = couponPeriod,
            usageLimitPolicy = usageLimitPolicy,
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
        minimumOrderAmount: Long? = null,
        discountType: DiscountType,
        discountValue: Int,
        issueStartAt: LocalDateTime,
        issueEndAt: LocalDateTime?,
        validityDays: Int?,
        useEndAt: LocalDateTime?,
        limitType: CouponTemplateLimitType,
        issueLimit: Long? = null,
        useLimit: Long? = null,
        status: CouponTemplateStatus,
        isIssuable: Boolean,
        isUsable: Boolean,
    ): CouponTemplate {
        return CouponTemplate(
            id = id,
            merchantId = merchantId,
            name = name,
            description = description,
            usageCondition = CouponUsageCondition(minimumOrderAmount),
            discountPolicy = DiscountPolicy(discountType, discountValue),
            couponPeriod = CouponPeriod(issueStartAt, issueEndAt, validityDays, useEndAt),
            usageLimitPolicy = UsageLimitPolicy(limitType, issueLimit, useLimit),
            status = status,
            isIssuable = isIssuable,
            isUsable = isUsable
        )
    }
}