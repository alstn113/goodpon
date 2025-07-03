package com.goodpon.core.domain.coupon.history

import java.time.LocalDateTime
import java.util.*

data class CouponHistory private constructor(
    val id: String,
    val userCouponId: String,
    val actionType: CouponActionType,
    val orderId: String?,
    val reason: String? = null,
    val recordedAt: LocalDateTime,
) {
    companion object {
        fun issued(
            userCouponId: String,
            recordedAt: LocalDateTime,
        ): CouponHistory = record(
            userCouponId = userCouponId,
            actionType = CouponActionType.ISSUE,
            recordedAt = recordedAt
        )

        fun redeemed(
            userCouponId: String,
            orderId: String,
            recordedAt: LocalDateTime,
        ): CouponHistory = record(
            userCouponId = userCouponId,
            actionType = CouponActionType.REDEEM,
            orderId = orderId,
            recordedAt = recordedAt
        )

        fun cancelRedemption(
            userCouponId: String,
            orderId: String,
            reason: String,
            recordedAt: LocalDateTime,
        ): CouponHistory = record(
            userCouponId = userCouponId,
            actionType = CouponActionType.CANCEL_REDEMPTION,
            orderId = orderId,
            reason = reason,
            recordedAt = recordedAt
        )

        fun expired(
            userCouponId: String,
            recordedAt: LocalDateTime,
        ): CouponHistory = record(
            userCouponId = userCouponId,
            actionType = CouponActionType.EXPIRE,
            recordedAt = recordedAt
        )

        fun discarded(
            userCouponId: String,
            recordedAt: LocalDateTime,
            reason: String,
        ): CouponHistory = record(
            userCouponId = userCouponId,
            actionType = CouponActionType.DISCARD,
            reason = reason,
            recordedAt = recordedAt
        )

        private fun record(
            userCouponId: String,
            actionType: CouponActionType,
            orderId: String? = null,
            reason: String? = null,
            recordedAt: LocalDateTime,
        ): CouponHistory {
            return CouponHistory(
                id = UUID.randomUUID().toString().replace("-", ""),
                userCouponId = userCouponId,
                actionType = actionType,
                orderId = orderId,
                reason = reason,
                recordedAt = recordedAt
            )
        }

        fun reconstruct(
            id: String,
            userCouponId: String,
            actionType: CouponActionType,
            orderId: String?,
            recordedAt: LocalDateTime,
        ): CouponHistory {
            return CouponHistory(
                id = id,
                userCouponId = userCouponId,
                actionType = actionType,
                orderId = orderId,
                recordedAt = recordedAt
            )
        }
    }
}