package com.goodpon.core.application.coupon.request

import com.goodpon.core.application.auth.MerchantPrincipal

data class IssueCouponRequest(
    val merchantPrincipal: MerchantPrincipal,
    val couponTemplateId: Long,
    val userId: String,
)