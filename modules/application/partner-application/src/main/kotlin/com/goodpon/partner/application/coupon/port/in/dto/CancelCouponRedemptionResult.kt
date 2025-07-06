package com.goodpon.partner.application.coupon.port.`in`.dto

import com.goodpon.domain.coupon.user.UserCouponStatus
import java.time.LocalDateTime

data class CancelCouponRedemptionResult(
    val userCouponId: String,
    val status: UserCouponStatus,
    val canceledAt: LocalDateTime,
    val cancelReason: String,
)
