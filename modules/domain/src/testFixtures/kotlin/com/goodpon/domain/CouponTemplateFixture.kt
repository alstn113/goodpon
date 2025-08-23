package com.goodpon.domain

import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.template.CouponTemplateFactory
import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import java.time.LocalDate

object CouponTemplateFixture {

    fun create(merchantId: Long, isPublished: Boolean = true): CouponTemplate {
        return CouponTemplateFactory.create(
            merchantId = merchantId,
            name = "테스트 쿠폰 템플릿",
            description = "테스트 쿠폰 템플릿 설명",
            minOrderAmount = 10000,
            discountType = CouponDiscountType.FIXED_AMOUNT,
            discountValue = 1000,
            maxDiscountAmount = null,
            issueStartDate = LocalDate.now(),
            issueEndDate = null,
            validityDays = null,
            absoluteExpiryDate = null,
            limitType = CouponLimitPolicyType.NONE,
            maxIssueCount = null,
            maxRedeemCount = null,
            status = if (isPublished) CouponTemplateStatus.ISSUABLE else CouponTemplateStatus.DRAFT,
        )
    }
}