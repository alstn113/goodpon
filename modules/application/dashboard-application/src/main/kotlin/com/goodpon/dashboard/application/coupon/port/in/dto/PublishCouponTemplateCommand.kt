package com.goodpon.dashboard.application.coupon.port.`in`.dto

data class PublishCouponTemplateCommand(
    val merchantId: Long,
    val couponTemplateId: Long,
    val accountId: Long,
)