package com.goodpon.partner.application.coupon.port.`in`.dto

data class RedeemCouponCommand(
    val merchantId: Long,
    val couponId: String,
    val userId: String,
    val orderAmount: Int,
    val orderId: String,
)
