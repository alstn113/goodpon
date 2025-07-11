package com.goodpon.partner.openapi.controller.v1.request

import com.goodpon.partner.application.coupon.port.`in`.dto.CancelCouponRedemptionCommand

data class CancelCouponRedemptionRequest(
    val cancelReason: String,
) {

    fun toCommand(merchantId: Long, couponId: String): CancelCouponRedemptionCommand {
        return CancelCouponRedemptionCommand(
            merchantId = merchantId,
            couponId = couponId,
            cancelReason = cancelReason,
        )
    }
}