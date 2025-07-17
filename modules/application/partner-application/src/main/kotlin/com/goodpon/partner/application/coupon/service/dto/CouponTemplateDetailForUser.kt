package com.goodpon.partner.application.coupon.service.dto

import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import com.goodpon.partner.application.coupon.port.`in`.dto.CouponIssuanceStatus
import java.time.LocalDateTime

data class CouponTemplateDetailForUser(
    val id: Long,
    val name: String,
    val description: String,
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
    val issuanceStatus: CouponIssuanceStatus,
)

data class CouponTemplateDetail(
    val id: Long,
    val name: String,
    val description: String,
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
    val issueCount: Long,
    val redeemCount: Long,
) {

    fun forUser(issuanceStatus: CouponIssuanceStatus): CouponTemplateDetailForUser {
        return CouponTemplateDetailForUser(
            id = id,
            name = name,
            description = description,
            discountType = discountType,
            discountValue = discountValue,
            maxDiscountAmount = maxDiscountAmount,
            minOrderAmount = minOrderAmount,
            status = status,
            validityDays = validityDays,
            absoluteExpiresAt = absoluteExpiresAt,
            issueStartAt = issueStartAt,
            issueEndAt = issueEndAt,
            limitType = limitType,
            maxIssueCount = maxIssueCount,
            maxRedeemCount = maxRedeemCount,
            currentIssueCount = issueCount,
            currentRedeemCount = redeemCount,
            issuanceStatus = issuanceStatus
        )
    }
}