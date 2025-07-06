package com.goodpon.dashboard.application.coupon.port.`in`.dto

import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType
import java.time.LocalDate

data class CreateCouponTemplateCommand(
    val accountId: Long,
    val name: String,
    val description: String,
    val merchantId: Long,
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
)
