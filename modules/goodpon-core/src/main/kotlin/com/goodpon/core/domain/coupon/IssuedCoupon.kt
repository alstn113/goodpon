package com.goodpon.core.domain.coupon

import java.time.LocalDateTime
import java.util.*

data class IssuedCoupon private constructor(
    val id: UUID,
    val couponTemplateId: Long,
    val userId: Long,
    val issuedAt: LocalDateTime,
    val expiresAt: LocalDateTime?,
    val isUsed: Boolean,
    val usedAt: LocalDateTime?,
) {

    fun markAsUsed(now: LocalDateTime = LocalDateTime.now()): IssuedCoupon {
        if (isUsed) {
            throw IllegalStateException("이미 사용된 쿠폰입니다.")
        }

        return copy(isUsed = true, usedAt = now)
    }

    companion object {

        fun issue(
            userId: Long,
            couponTemplateId: Long,
            expiresAt: LocalDateTime?,
            now: LocalDateTime,
        ): IssuedCoupon {
            return IssuedCoupon(
                id = UUID.randomUUID(),
                couponTemplateId = couponTemplateId,
                userId = userId,
                issuedAt = now,
                expiresAt = expiresAt,
                isUsed = false,
                usedAt = null,
            )
        }

        fun reconstitute(
            id: UUID,
            couponTemplateId: Long,
            userId: Long,
            issuedAt: LocalDateTime,
            expiresAt: LocalDateTime?,
            isUsed: Boolean,
            usedAt: LocalDateTime?,
        ): IssuedCoupon {
            return IssuedCoupon(
                id = id,
                couponTemplateId = couponTemplateId,
                userId = userId,
                issuedAt = issuedAt,
                expiresAt = expiresAt,
                isUsed = isUsed,
                usedAt = usedAt
            )
        }
    }
}
