package com.goodpon.core.application.coupon.request

import com.goodpon.core.domain.auth.MerchantPrincipal

data class CancelCouponUsageRequest(
    val couponId: String,
    val merchantPrincipal: MerchantPrincipal,
    val userId: String,
    val cancelReason: String,
)
