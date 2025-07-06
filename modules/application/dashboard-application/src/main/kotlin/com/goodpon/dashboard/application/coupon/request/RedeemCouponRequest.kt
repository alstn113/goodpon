package com.goodpon.dashboard.application.coupon.request

data class RedeemCouponRequest(
    val merchantId: Long,
    val couponId: String,
    val userId: String,
    val orderAmount: Int,
    val orderId: String,
)
