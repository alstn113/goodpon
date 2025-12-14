package com.goodpon.partner.application.coupon.service.dto

import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType
import java.time.LocalDateTime

data class UserCouponList(
    val coupons: List<UserCouponWithRedeemable>,
)

data class UserCouponSummary(
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
) {

    fun withRedeemable(isRedeemable: Boolean): UserCouponWithRedeemable {
        return UserCouponWithRedeemable(
            userCouponId = this.userCouponId,
            couponTemplateId = this.couponTemplateId,
            couponTemplateName = this.couponTemplateName,
            couponTemplateDescription = this.couponTemplateDescription,
            discountType = this.discountType,
            discountValue = this.discountValue,
            maxDiscountAmount = this.maxDiscountAmount,
            minOrderAmount = this.minOrderAmount,
            issuedAt = this.issuedAt,
            expiresAt = this.expiresAt,
            limitType = this.limitType,
            maxIssueCount = this.maxIssueCount,
            maxRedeemCount = this.maxRedeemCount,
            isRedeemable = isRedeemable
        )
    }
}

data class UserCouponWithRedeemable(
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
    val isRedeemable: Boolean,
)
