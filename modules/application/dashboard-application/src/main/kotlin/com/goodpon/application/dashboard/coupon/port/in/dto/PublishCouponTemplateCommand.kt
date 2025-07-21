package com.goodpon.application.dashboard.coupon.port.`in`.dto

data class PublishCouponTemplateCommand(
    val merchantId: Long,
    val couponTemplateId: Long,
    val accountId: Long,
)