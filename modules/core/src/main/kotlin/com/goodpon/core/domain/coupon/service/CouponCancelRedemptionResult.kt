package com.goodpon.core.domain.coupon.service

import com.goodpon.core.domain.coupon.user.UserCouponStatus
import java.time.LocalDateTime

data class CouponCancelRedemptionResult(
    val userCouponId: String,
    val status: UserCouponStatus,
    val canceledAt: LocalDateTime,
    val cancelReason: String,
)
