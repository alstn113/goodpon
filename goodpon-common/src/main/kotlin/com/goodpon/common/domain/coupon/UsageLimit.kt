package com.goodpon.common.domain.coupon

data class UsageLimit(
    val limitType: CouponTemplateLimitType,
    val issueLimit: Long? = null,
    val useLimit: Long? = null,
    val issueCount: Long = 0,
    val useCount: Long = 0,
)
