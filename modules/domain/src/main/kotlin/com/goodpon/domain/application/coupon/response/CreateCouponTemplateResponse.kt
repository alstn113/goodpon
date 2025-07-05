package com.goodpon.domain.application.coupon.response

import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.domain.coupon.template.vo.CouponLimitPolicyType
import java.time.LocalDateTime

data class CreateCouponTemplateResponse(
    val id: Long,
    val name: String,
    val description: String,
    val merchantId: Long,
    val minOrderAmount: Int?,
    val discountType: CouponDiscountType,
    val discountValue: Int,
    val maxDiscountAmount: Int?,
    val issueStartAt: LocalDateTime,
    val issueEndAt: LocalDateTime?,
    val validityDays: Int?,
    val absoluteExpiresAt: LocalDateTime?,
    val limitType: CouponLimitPolicyType,
    val maxIssueCount: Long?,
    val maxRedeemCount: Long?,
) {

    companion object {
        fun from(couponTemplate: com.goodpon.domain.coupon.template.CouponTemplate): CreateCouponTemplateResponse {
            return CreateCouponTemplateResponse(
                id = couponTemplate.id,
                name = couponTemplate.name,
                description = couponTemplate.description,
                merchantId = couponTemplate.merchantId,
                minOrderAmount = couponTemplate.redemptionCondition.minOrderAmount,
                discountType = couponTemplate.discountPolicy.discountType,
                discountValue = couponTemplate.discountPolicy.discountValue,
                maxDiscountAmount = couponTemplate.discountPolicy.maxDiscountAmount,
                issueStartAt = couponTemplate.period.issueStartAt,
                issueEndAt = couponTemplate.period.issueEndAt,
                validityDays = couponTemplate.period.validityDays,
                absoluteExpiresAt = couponTemplate.period.absoluteExpiresAt,
                limitType = couponTemplate.limitPolicy.limitType,
                maxIssueCount = couponTemplate.limitPolicy.maxIssueCount,
                maxRedeemCount = couponTemplate.limitPolicy.maxRedeemCount,
            )
        }
    }
}