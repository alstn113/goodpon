package com.goodpon.dashboard.application.coupon.service

import com.goodpon.dashboard.application.coupon.port.`in`.dto.CreateCouponTemplateCommand
import com.goodpon.dashboard.application.coupon.port.`in`.dto.CreateCouponTemplateResult
import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.template.CouponTemplateFactory

object CouponTemplateMapper {

    fun toCouponTemplate(command: CreateCouponTemplateCommand): CouponTemplate {
        return CouponTemplateFactory.create(
            merchantId = command.merchantId,
            name = command.name,
            description = command.description,
            minOrderAmount = command.minOrderAmount,
            discountType = command.discountType,
            discountValue = command.discountValue,
            maxDiscountAmount = command.maxDiscountAmount,
            issueStartDate = command.issueStartDate,
            issueEndDate = command.issueEndDate,
            validityDays = command.validityDays,
            absoluteExpiryDate = command.absoluteExpiryDate,
            limitType = command.limitType,
            maxIssueCount = command.maxIssueCount,
            maxRedeemCount = command.maxRedeemCount,
        )
    }

    fun toCreateCouponTemplateResult(couponTemplate: CouponTemplate): CreateCouponTemplateResult {
        return CreateCouponTemplateResult(
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
            status = couponTemplate.status,
        )
    }
}