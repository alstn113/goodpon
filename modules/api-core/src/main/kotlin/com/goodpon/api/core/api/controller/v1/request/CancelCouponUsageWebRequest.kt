package com.goodpon.api.core.api.controller.v1.request

import com.goodpon.core.application.coupon.request.CancelCouponUsageRequest
import com.goodpon.core.domain.auth.MerchantPrincipal

data class CancelCouponUsageWebRequest(
    val cancelReason: String,
    val userId: String,
) {

    fun toAppRequest(merchantPrincipal: MerchantPrincipal, couponId: String): CancelCouponUsageRequest {
        return CancelCouponUsageRequest(
            merchantPrincipal = merchantPrincipal,
            couponId = couponId,
            userId = userId,
            cancelReason = cancelReason,
        )
    }
}