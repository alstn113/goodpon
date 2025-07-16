package com.goodpon.partner.openapi.controller.v1.request

import com.goodpon.partner.application.coupon.port.`in`.dto.RedeemCouponCommand

data class RedeemCouponRequest(
    val userId: String,
    val orderAmount: Int,
    val orderId: String,
) {

    fun toCommand(merchantId: Long, userCouponId: String): RedeemCouponCommand {
        return RedeemCouponCommand(
            merchantId = merchantId,
            userCouponId = userCouponId,
            userId = userId,
            orderAmount = orderAmount,
            orderId = orderId
        )
    }
}
