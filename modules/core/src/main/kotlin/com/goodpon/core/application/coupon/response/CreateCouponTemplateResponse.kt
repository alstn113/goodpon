package com.goodpon.core.application.coupon.response

import com.goodpon.core.domain.coupon.CouponTemplate
import com.goodpon.core.domain.coupon.vo.CouponDiscountType
import com.goodpon.core.domain.coupon.vo.CouponLimitType
import java.time.LocalDateTime

data class CreateCouponTemplateResponse(
    val id: Long,
    val name: String,
    val description: String,
    val merchantId: Long,
    val minOrderAmount: Long?,
    val discountType: CouponDiscountType,
    val discountValue: Int,
    val maxDiscountAmount: Int?,
    val issueStartAt: LocalDateTime,
    val issueEndAt: LocalDateTime?,
    val validityDays: Int?,
    val absoluteExpiresAt: LocalDateTime?,
    val limitType: CouponLimitType,
    val maxIssueLimit: Long?,
    val maxRedeemLimit: Long?,
) {

    companion object {
        fun from(couponTemplate: CouponTemplate): CreateCouponTemplateResponse {
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
                maxIssueLimit = couponTemplate.limitPolicy.maxIssueLimit,
                maxRedeemLimit = couponTemplate.limitPolicy.maxRedeemLimit,
            )
        }
    }
}