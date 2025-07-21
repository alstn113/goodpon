package com.goodpon.application.dashboard.coupon.port.`in`.dto

import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import java.time.LocalDateTime

data class CreateCouponTemplateResult(
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
    val status: CouponTemplateStatus,
)