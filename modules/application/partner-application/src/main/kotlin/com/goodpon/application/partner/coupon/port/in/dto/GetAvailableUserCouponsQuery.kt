package com.goodpon.application.partner.coupon.port.`in`.dto

data class GetAvailableUserCouponsQuery(
    val merchantId: Long,
    val userId: String,
    val orderAmount: Int,
)
