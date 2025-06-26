package com.goodpon.api.core.api.controller.v1.request

import com.goodpon.core.application.coupon.request.IssueCouponRequest
import com.goodpon.core.domain.auth.MerchantPrincipal

data class IssueCouponWebRequest(
    val couponTemplateId: Long,
    val userId: String,
) {

    fun toAppRequest(merchantPrincipal: MerchantPrincipal): IssueCouponRequest {
        return IssueCouponRequest(
            merchantPrincipal = merchantPrincipal,
            couponTemplateId = couponTemplateId,
            userId = userId,
        )
    }
}
