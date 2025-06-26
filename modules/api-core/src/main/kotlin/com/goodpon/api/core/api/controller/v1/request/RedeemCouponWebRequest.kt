package com.goodpon.api.core.api.controller.v1.request

import com.goodpon.core.application.coupon.request.RedeemCouponRequest
import com.goodpon.core.domain.auth.MerchantPrincipal

data class RedeemCouponWebRequest(
    val userId: String,
    val orderAmount: Int,
    val orderId: String
) {

    fun toAppRequest(merchantPrincipal: MerchantPrincipal, couponId: String): RedeemCouponRequest {
        return RedeemCouponRequest(
            merchantPrincipal = merchantPrincipal,
            couponId = couponId,
            userId = userId,
            orderAmount = orderAmount,
            orderId = orderId
        )
    }
}
