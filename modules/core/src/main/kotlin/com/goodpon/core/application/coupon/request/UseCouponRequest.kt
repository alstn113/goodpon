package com.goodpon.core.application.coupon.request

import com.goodpon.core.domain.auth.MerchantPrincipal

data class UseCouponRequest(
    val merchantPrincipal: MerchantPrincipal,
    val couponId: String,
    val userId: String,
    val orderAmount: Int,
)
