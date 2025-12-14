package com.goodpon.infra.jpa.repository.dto

import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType
import java.time.LocalDateTime

data class UserCouponSummaryDto(
    val userCouponId: String,
    val couponTemplateId: Long,
    val couponTemplateName: String,
    val couponTemplateDescription: String,
    val discountType: CouponDiscountType,
    val discountValue: Int,
    val maxDiscountAmount: Int?,
    val minOrderAmount: Int?,
    val issuedAt: LocalDateTime,
    val expiresAt: LocalDateTime?,
    val limitType: CouponLimitPolicyType,
    val maxIssueCount: Long?,
    val maxRedeemCount: Long?,
)