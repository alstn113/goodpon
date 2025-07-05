package com.goodpon.api.core.api.controller.v1.request

import com.goodpon.core.application.coupon.request.CancelCouponRedemptionRequest

data class CancelCouponRedemptionWebRequest(
    val cancelReason: String,
) {

    fun toAppRequest(merchantId: Long, couponId: String): CancelCouponRedemptionRequest {
        return CancelCouponRedemptionRequest(
            merchantId = merchantId,
            couponId = couponId,
            cancelReason = cancelReason,
        )
    }
}