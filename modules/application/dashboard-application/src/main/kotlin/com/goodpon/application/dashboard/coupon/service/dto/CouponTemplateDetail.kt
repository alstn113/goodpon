package com.goodpon.application.dashboard.coupon.service.dto

import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import java.time.LocalDateTime

data class CouponTemplateDetail(
    val id: Long,
    val merchantId: Long,
    val name: String,
    val description: String,
    val minOrderAmount: Int?,
    val discountType: CouponDiscountType,
    val discountValue: Int,
    val maxDiscountAmount: Int?,
    val status: CouponTemplateStatus,
    val issueStartAt: LocalDateTime,
    val issueEndAt: LocalDateTime?,
    val validityDays: Int?,
    val absoluteExpiresAt: LocalDateTime?,
    val limitType: CouponLimitPolicyType,
    val maxIssueCount: Long?,
    val maxRedeemCount: Long?,
    val issueCount: Long,
    val redeemCount: Long,
    val createdAt: LocalDateTime,
)
