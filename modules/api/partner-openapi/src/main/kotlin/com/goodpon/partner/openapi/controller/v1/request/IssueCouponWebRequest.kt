package com.goodpon.partner.openapi.controller.v1.request

import com.goodpon.partner.application.coupon.request.IssueCouponRequest

data class IssueCouponWebRequest(
    val couponTemplateId: Long,
    val userId: String,
) {

    fun toAppRequest(merchantId: Long): IssueCouponRequest {
        return IssueCouponRequest(
            merchantId = merchantId,
            couponTemplateId = couponTemplateId,
            userId = userId,
        )
    }
}
