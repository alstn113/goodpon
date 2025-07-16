package com.goodpon.partner.application.coupon.service.dto

import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import java.time.LocalDateTime

data class CouponTemplateStatusResult(
    val couponTemplateId: Long,
    val couponTemplateName: String,
    val couponTemplateDescription: String,
    val discountType: CouponDiscountType,
    val discountValue: Int,
    val maxDiscountAmount: Int?,
    val minOrderAmount: Int?,
    val status: CouponTemplateStatus,
    val validityDays: Int?,
    val absoluteExpiresAt: LocalDateTime?,
    val issueStartAt: LocalDateTime,
    val issueEndAt: LocalDateTime?,
    val limitType: CouponLimitPolicyType,
    val maxIssueCount: Long?,
    val maxRedeemCount: Long?,
    val currentIssueCount: Long,
    val currentRedeemCount: Long,
    val alreadyIssued: Boolean?, // 사용자별 발급 여부
    val isIssuable: Boolean, // 발급 수 제한 시 발급 가능 여부 체크
)