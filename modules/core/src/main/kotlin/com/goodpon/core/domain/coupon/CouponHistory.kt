package com.goodpon.core.domain.coupon

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
            now: LocalDateTime = LocalDateTime.now(),
        ): CouponHistory = record(
            userCouponId = userCouponId,
            actionType = CouponActionType.ISSUE,
            now = now
        )

        fun redeemed(
            userCouponId: String,
            orderId: String,
            now: LocalDateTime = LocalDateTime.now(),
        ): CouponHistory = record(
            userCouponId = userCouponId,
            actionType = CouponActionType.REDEEM,
            orderId = orderId,
            now = now
        )

        fun cancelRedemption(
            userCouponId: String,
            orderId: String,
            reason: String,
            now: LocalDateTime = LocalDateTime.now(),
        ): CouponHistory = record(
            userCouponId = userCouponId,
            actionType = CouponActionType.CANCEL_REDEMPTION,
            orderId = orderId,
            reason = reason,
            now = now
        )

        fun expired(
            userCouponId: String,
            now: LocalDateTime = LocalDateTime.now(),
        ): CouponHistory = record(
            userCouponId = userCouponId,
            actionType = CouponActionType.EXPIRE,
            now = now
        )

        fun discarded(
            userCouponId: String,
            now: LocalDateTime = LocalDateTime.now(),
            reason: String,
        ): CouponHistory = record(
            userCouponId = userCouponId,
            actionType = CouponActionType.DISCARD,
            reason = reason,
            now = now
        )

        private fun record(
            userCouponId: String,
            actionType: CouponActionType,
            orderId: String? = null,
            reason: String? = null,
            now: LocalDateTime = LocalDateTime.now(),
        ): CouponHistory {
            return CouponHistory(
                id = UUID.randomUUID().toString().replace("-", ""),
                userCouponId = userCouponId,
                actionType = actionType,
                orderId = orderId,
                reason = reason,
                recordedAt = now
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