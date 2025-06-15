package com.goodpon.core.domain.coupon

import java.time.LocalDateTime
import java.util.*

data class IssuedCoupon(
    val id: UUID,
    val couponTemplateId: Long,
    val accountId: Long,
    val issuedAt: LocalDateTime,
    val expiresAt: LocalDateTime?,
    val isUsed: Boolean,
    val usedAt: LocalDateTime?,
) {

    companion object {

        fun issue(
            accountId: Long,
            couponTemplateId: Long,
            expiresAt: LocalDateTime?,
            now: LocalDateTime,
        ): IssuedCoupon {

            return IssuedCoupon(
                id = UUID.randomUUID(),
                couponTemplateId = couponTemplateId,
                accountId = accountId,
                issuedAt = now,
                expiresAt = expiresAt,
                isUsed = false,
                usedAt = null,
            )
        }
    }

    fun use(now: LocalDateTime): IssuedCoupon {
        if (isUsed) {
            throw IllegalStateException("이미 사용된 쿠폰입니다.")
        }

        return copy(isUsed = true, usedAt = now)
    }
}
