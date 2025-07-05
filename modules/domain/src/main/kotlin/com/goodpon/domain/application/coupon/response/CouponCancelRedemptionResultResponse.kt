package com.goodpon.domain.application.coupon.response

import com.goodpon.domain.domain.coupon.user.UserCouponStatus
import java.time.LocalDateTime

data class CouponCancelRedemptionResultResponse(
    val userCouponId: String,
    val status: UserCouponStatus,
    val canceledAt: LocalDateTime,
    val cancelReason: String,
)
