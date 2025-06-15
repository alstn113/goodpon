package com.goodpon.core.domain.coupon

data class CouponTemplateStats(
    val couponTemplateId: Long,
    val issueCount: Long,
    val useCount: Long,
)