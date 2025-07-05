package com.goodpon.api.core.api.controller.v1.request

import com.goodpon.core.application.coupon.request.CancelCouponRedemptionRequest
import com.goodpon.core.application.auth.MerchantPrincipal

data class CancelCouponRedemptionWebRequest(
    val cancelReason: String,
) {

    fun toAppRequest(merchantPrincipal: MerchantPrincipal, couponId: String): CancelCouponRedemptionRequest {
        return CancelCouponRedemptionRequest(
            merchantPrincipal = merchantPrincipal,
            couponId = couponId,
            cancelReason = cancelReason,
        )
    }
}