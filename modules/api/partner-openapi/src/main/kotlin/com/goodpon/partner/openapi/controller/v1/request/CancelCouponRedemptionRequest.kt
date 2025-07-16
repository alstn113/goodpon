package com.goodpon.partner.openapi.controller.v1.request

import com.goodpon.partner.application.coupon.port.`in`.dto.CancelCouponRedemptionCommand

data class CancelCouponRedemptionRequest(
    val cancelReason: String,
    val orderId: String,
) {

    fun toCommand(merchantId: Long, userCouponId: String): CancelCouponRedemptionCommand {
        return CancelCouponRedemptionCommand(
            merchantId = merchantId,
            userCouponId = userCouponId,
            orderId = orderId,
            cancelReason = cancelReason,
        )
    }
}