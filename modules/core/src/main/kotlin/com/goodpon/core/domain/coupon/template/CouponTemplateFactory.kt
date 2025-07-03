package com.goodpon.core.domain.coupon.template

import com.goodpon.core.domain.coupon.template.vo.*
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
        expiryDate: LocalDate?,
        limitType: CouponLimitPolicyType,
        maxIssueCount: Long? = null,
        maxRedeemCount: Long? = null,
    ): CouponTemplate {
        val redemptionCondition = CouponRedemptionCondition(minOrderAmount = minOrderAmount)
        val discountPolicy = CouponDiscountPolicy(
            discountType = discountType,
            discountValue = discountValue,
            maxDiscountAmount = maxDiscountAmount
        )
        val period = CouponPeriodFactory.create(
            issueStartDate = issueStartDate,
            issueEndDate = issueEndDate,
            validityDays = validityDays,
            absoluteExpiryDate = expiryDate
        )
        val limitPolicy = CouponLimitPolicy(
            limitType = limitType,
            maxIssueCount = maxIssueCount,
            maxRedeemCount = maxRedeemCount
        )

        return CouponTemplate(
            id = 0,
            merchantId = merchantId,
            name = name,
            description = description,
            redemptionCondition = redemptionCondition,
            discountPolicy = discountPolicy,
            period = period,
            limitPolicy = limitPolicy,
            status = CouponTemplateStatus.DRAFT,
        )
    }

    fun reconstruct(
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
        absoluteExpiresAt: LocalDateTime?,
        limitType: CouponLimitPolicyType,
        maxIssueCount: Long? = null,
        maxRedeemCount: Long? = null,
        status: CouponTemplateStatus,
    ): CouponTemplate {
        return CouponTemplate(
            id = id,
            merchantId = merchantId,
            name = name,
            description = description,
            redemptionCondition = CouponRedemptionCondition(minOrderAmount),
            discountPolicy = CouponDiscountPolicy(discountType, discountValue, maxDiscountAmount),
            period = CouponPeriod(issueStartAt, issueEndAt, validityDays, absoluteExpiresAt),
            limitPolicy = CouponLimitPolicy(limitType, maxIssueCount, maxRedeemCount),
            status = status,
        )
    }
}