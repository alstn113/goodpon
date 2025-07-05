package com.goodpon.partner.openapi.controller.v1.request

import com.goodpon.domain.application.coupon.request.CancelCouponRedemptionRequest

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