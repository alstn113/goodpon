package com.goodpon.api.dashboard.api.controller.v1.request

import com.goodpon.core.application.coupon.request.CreateCouponTemplateRequest
import com.goodpon.core.domain.auth.AccountPrincipal
import com.goodpon.core.domain.coupon.vo.CouponDiscountType
import com.goodpon.core.domain.coupon.vo.CouponLimitType
import java.time.LocalDate

data class CreateCouponTemplateWebRequest(
    val name: String,
    val description: String,
    val minOrderAmount: Long?,
    val discountType: CouponDiscountType,
    val discountValue: Int,
    val maxDiscountAmount: Int?,
    val issueStartDate: LocalDate,
    val issueEndDate: LocalDate?,
    val validityDays: Int?,
    val redeemEndDate: LocalDate?,
    val limitType: CouponLimitType,
    val maxIssueLimit: Long?,
    val maxRedeemLimit: Long?,
) {

    fun toAppRequest(merchantId: Long, accountPrincipal: AccountPrincipal): CreateCouponTemplateRequest {
        return CreateCouponTemplateRequest(
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
            expiryDate = redeemEndDate,
            limitType = limitType,
            maxIssueLimit = maxIssueLimit,
            maxRedeemLimit = maxRedeemLimit,
            accountPrincipal = accountPrincipal
        )
    }
}