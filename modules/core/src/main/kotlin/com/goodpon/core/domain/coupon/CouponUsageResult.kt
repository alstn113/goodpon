package com.goodpon.core.domain.coupon

import java.time.LocalDateTime
import java.util.*

data class CouponUsageResult(
    val couponId: String,
    val discountAmount: Int,
    val originalPrice: Int,
    val finalPrice: Int,
    val usedAt: LocalDateTime
)
