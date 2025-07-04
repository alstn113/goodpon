package com.goodpon.core.application.coupon.request

import com.goodpon.core.application.auth.MerchantPrincipal

data class RedeemCouponRequest(
    val merchantPrincipal: MerchantPrincipal,
    val couponId: String,
    val userId: String,
    val orderAmount: Int,
    val orderId: String,
)
