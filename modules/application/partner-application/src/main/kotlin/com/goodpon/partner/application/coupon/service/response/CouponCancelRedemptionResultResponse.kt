package com.goodpon.partner.application.coupon.service.response

import com.goodpon.domain.coupon.user.UserCouponStatus
import java.time.LocalDateTime

data class CouponCancelRedemptionResultResponse(
    val userCouponId: String,
    val status: UserCouponStatus,
    val canceledAt: LocalDateTime,
    val cancelReason: String,
)
