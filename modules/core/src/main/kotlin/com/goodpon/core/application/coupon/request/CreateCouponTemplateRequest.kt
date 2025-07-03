package com.goodpon.core.application.coupon.request

import com.goodpon.core.application.auth.AccountPrincipal
import com.goodpon.core.domain.coupon.CouponTemplate
import com.goodpon.core.domain.coupon.CouponTemplateFactory
import com.goodpon.core.domain.coupon.vo.CouponDiscountType
import com.goodpon.core.domain.coupon.vo.CouponLimitType
import java.time.LocalDate

data class CreateCouponTemplateRequest(
    val accountPrincipal: AccountPrincipal,
    val name: String,
    val description: String,
    val merchantId: Long,
    val minOrderAmount: Long?,
    val discountType: CouponDiscountType,
    val discountValue: Int,
    val maxDiscountAmount: Int?,
    val issueStartDate: LocalDate,
    val issueEndDate: LocalDate?,
    val validityDays: Int?,
    val expiryDate: LocalDate?,
    val limitType: CouponLimitType,
    val maxIssueLimit: Long?,
    val maxRedeemLimit: Long?,
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
            expiryDate = expiryDate,
            limitType = limitType,
            maxIssueLimit = maxIssueLimit,
            maxRedeemLimit = maxRedeemLimit,
        )
    }
}