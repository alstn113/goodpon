package com.goodpon.api.partner.controller.v1.request

import com.goodpon.application.partner.coupon.port.`in`.dto.IssueCouponCommand

data class IssueCouponRequest(
    val userId: String,
) {

    fun toCommand(merchantId: Long, couponTemplateId: Long): IssueCouponCommand {
        return IssueCouponCommand(
            merchantId = merchantId,
            couponTemplateId = couponTemplateId,
            userId = userId,
        )
    }
}
