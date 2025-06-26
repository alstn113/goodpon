package com.goodpon.core.application.coupon.request

import com.goodpon.core.domain.auth.MerchantPrincipal
import com.goodpon.core.domain.coupon.CouponTemplate
import com.goodpon.core.domain.coupon.CouponTemplateFactory
import com.goodpon.core.domain.coupon.vo.CouponDiscountType
import com.goodpon.core.domain.coupon.vo.CouponLimitType
import java.time.LocalDate

data class CreateCouponTemplateRequest(
    val merchantPrincipal: MerchantPrincipal,
    val name: String,
    val description: String,
    val minOrderAmount: Long?,
    val discountType: CouponDiscountType,
    val discountValue: Int,
    val maxDiscountAmount: Int?,
    val issueStartDate: LocalDate,
    val issueEndDate: LocalDate?,
    val validityDays: Int?,
    val usageEndDate: LocalDate?,
    val limitType: CouponLimitType,
    val maxIssueLimit: Long?,
    val maxUsageLimit: Long?,
) {

    fun toCouponTemplate(): CouponTemplate {
        return CouponTemplateFactory.create(
            merchantId = merchantPrincipal.merchantId,
            name = name,
            description = description,
            minOrderAmount = minOrderAmount,
            discountType = discountType,
            discountValue = discountValue,
            maxDiscountAmount = maxDiscountAmount,
            issueStartDate = issueStartDate,
            issueEndDate = issueEndDate,
            validityDays = validityDays,
            usageEndDate = usageEndDate,
            limitType = limitType,
            maxIssueLimit = maxIssueLimit,
            maxUsageLimit = maxUsageLimit,
        )
    }
}