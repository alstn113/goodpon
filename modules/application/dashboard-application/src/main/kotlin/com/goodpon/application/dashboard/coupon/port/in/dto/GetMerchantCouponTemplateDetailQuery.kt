package com.goodpon.application.dashboard.coupon.port.`in`.dto

data class GetMerchantCouponTemplateDetailQuery(
    val accountId: Long,
    val merchantId: Long,
    val couponTemplateId: Long,
)
