package com.goodpon.core.domain.coupon.service

import com.goodpon.core.domain.coupon.CouponStatus
import java.time.LocalDateTime

data class CouponCancelRedemptionResult(
    val userCouponId: String,
    val status: CouponStatus,
    val canceledAt: LocalDateTime,
    val cancelReason: String,
)
