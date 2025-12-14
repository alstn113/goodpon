package com.goodpon.partner.application.coupon.port.`in`.dto

data class RedeemCouponCommand(
    val merchantId: Long,
    val userCouponId: String,
    val userId: String,
    val orderAmount: Int,
    val orderId: String,
)
