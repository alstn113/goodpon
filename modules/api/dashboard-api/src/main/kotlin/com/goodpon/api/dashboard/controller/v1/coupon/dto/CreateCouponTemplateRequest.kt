package com.goodpon.api.dashboard.controller.v1.coupon.dto

import com.goodpon.application.dashboard.coupon.port.`in`.dto.CreateCouponTemplateCommand
import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType
import java.time.LocalDate

data class CreateCouponTemplateRequest(
    val name: String,
    val description: String,
    val minOrderAmount: Int?,
    val discountType: CouponDiscountType,
    val discountValue: Int,
    val maxDiscountAmount: Int?,
    val issueStartDate: LocalDate,
    val issueEndDate: LocalDate?,
    val validityDays: Int?,
    val absoluteExpiryDate: LocalDate?,
    val limitType: CouponLimitPolicyType,
    val maxIssueCount: Long?,
    val maxRedeemCount: Long?,
) {

    fun toCommand(merchantId: Long, accountId: Long): CreateCouponTemplateCommand {
        return CreateCouponTemplateCommand(
            accountId = accountId,
            merchantId = merchantId,
            name = name,
            description = description,
            minOrderAmount = minOrderAmount,
            discountType = discountType,
            discountValue = discountValue,
            maxDiscountAmount = maxDiscountAmount,
            issueStartDate = issueStartDate,
            issueEndDate = issueEndDate,
            validityDays = validityDays,
            absoluteExpiryDate = absoluteExpiryDate,
            limitType = limitType,
            maxIssueCount = maxIssueCount,
            maxRedeemCount = maxRedeemCount
        )
    }
}