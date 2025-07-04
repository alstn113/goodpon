package com.goodpon.core.application.coupon.request

import com.goodpon.core.application.auth.MerchantPrincipal

data class CancelCouponRedemptionRequest(
    val couponId: String,
    val merchantPrincipal: MerchantPrincipal,
    val cancelReason: String,
)
