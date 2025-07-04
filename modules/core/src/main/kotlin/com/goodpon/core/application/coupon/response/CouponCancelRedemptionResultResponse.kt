package com.goodpon.core.application.coupon.response

import com.goodpon.core.domain.coupon.user.UserCouponStatus
import java.time.LocalDateTime

data class CouponCancelRedemptionResultResponse(
    val userCouponId: String,
    val status: UserCouponStatus,
    val canceledAt: LocalDateTime,
    val cancelReason: String,
)
