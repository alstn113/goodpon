package com.goodpon.core.domain.coupon

import java.time.LocalDateTime
import java.util.*

data class Coupon private constructor(
    val id: UUID,
    val couponTemplateId: Long,
    val accountId: Long,
    val issuedAt: LocalDateTime,
    val expiresAt: LocalDateTime?,
    val isUsed: Boolean,
    val usedAt: LocalDateTime?,
) {

    fun use(now: LocalDateTime = LocalDateTime.now()): Coupon {
        if (isUsed) {
            throw IllegalStateException("이미 사용된 쿠폰입니다.")
        }

        return copy(isUsed = true, usedAt = now)
    }

    companion object {

        fun issue(
            accountId: Long,
            couponTemplateId: Long,
            expiresAt: LocalDateTime?,
            now: LocalDateTime,
        ): Coupon {
            return Coupon(
                id = UUID.randomUUID(),
                couponTemplateId = couponTemplateId,
                accountId = accountId,
                issuedAt = now,
                expiresAt = expiresAt,
                isUsed = false,
                usedAt = null,
            )
        }

        fun reconstitute(
            id: UUID,
            couponTemplateId: Long,
            accountId: Long,
            issuedAt: LocalDateTime,
            expiresAt: LocalDateTime?,
            isUsed: Boolean,
            usedAt: LocalDateTime?,
        ): Coupon {
            return Coupon(
                id = id,
                couponTemplateId = couponTemplateId,
                accountId = accountId,
                issuedAt = issuedAt,
                expiresAt = expiresAt,
                isUsed = isUsed,
                usedAt = usedAt
            )
        }
    }
}
