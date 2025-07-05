package com.goodpon.api.core.api.controller.v1.request

import com.goodpon.core.application.coupon.request.RedeemCouponRequest

data class RedeemCouponWebRequest(
    val userId: String,
    val orderAmount: Int,
    val orderId: String,
) {

    fun toAppRequest(merchantId: Long, couponId: String): RedeemCouponRequest {
        return RedeemCouponRequest(
            merchantId = merchantId,
            couponId = couponId,
            userId = userId,
            orderAmount = orderAmount,
            orderId = orderId
        )
    }
}
