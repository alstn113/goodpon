package com.goodpon.core.application.coupon.request

import com.goodpon.core.application.auth.AccountPrincipal
import com.goodpon.core.domain.coupon.template.CouponTemplate
import com.goodpon.core.domain.coupon.template.CouponTemplateFactory
import com.goodpon.core.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.core.domain.coupon.template.vo.CouponLimitPolicyType
import java.time.LocalDate

data class CreateCouponTemplateRequest(
    val accountPrincipal: AccountPrincipal,
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
    val expiryDate: LocalDate?,
    val limitType: CouponLimitPolicyType,
    val maxIssueCount: Long?,
    val maxRedeemCount: Long?,
) {
    fun toCouponTemplate(): CouponTemplate {
        return CouponTemplateFactory.create(
            name = name,
            description = description,
            merchantId = merchantId,
            minOrderAmount = minOrderAmount,
            discountType = discountType,
            discountValue = discountValue,
            maxDiscountAmount = maxDiscountAmount,
            issueStartDate = issueStartDate,
            issueEndDate = issueEndDate,
            validityDays = validityDays,
            absoluteExpiryDate = expiryDate,
            limitType = limitType,
            maxIssueCount = maxIssueCount,
            maxRedeemCount = maxRedeemCount,
        )
    }
}