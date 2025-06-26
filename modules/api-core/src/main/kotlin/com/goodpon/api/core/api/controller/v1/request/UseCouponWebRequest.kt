package com.goodpon.api.core.api.controller.v1.request

import com.goodpon.core.application.coupon.request.UseCouponRequest
import com.goodpon.core.domain.auth.MerchantPrincipal

data class UseCouponWebRequest(
    val userId: String,
    val orderAmount: Int,
) {

    fun toAppRequest(merchantPrincipal: MerchantPrincipal, couponId: String): UseCouponRequest {
        return UseCouponRequest(
            merchantPrincipal = merchantPrincipal,
            couponId = couponId,
            userId = userId,
            orderAmount = orderAmount,
        )
    }
}
