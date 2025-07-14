package com.goodpon.dashboard.application.coupon.port.`in`.dto

data class GetMerchantCouponTemplateDetailQuery(
    val accountId: Long,
    val merchantId: Long,
    val couponTemplateId: Long,
)
