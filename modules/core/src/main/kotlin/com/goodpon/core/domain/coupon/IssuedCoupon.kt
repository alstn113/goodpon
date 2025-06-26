package com.goodpon.core.domain.coupon

import java.time.LocalDateTime
import java.util.*

data class IssuedCoupon private constructor(
    val id: String,
    val couponTemplateId: Long,
    val userId: String,
    val issuedAt: LocalDateTime,
    val expiresAt: LocalDateTime?,
    val isUsed: Boolean,
    val usedAt: LocalDateTime?,
) {

    fun use(now: LocalDateTime = LocalDateTime.now()): IssuedCoupon {
        if (isUsed) {
            throw IllegalStateException("이미 사용된 쿠폰입니다.")
        }

        if (expiresAt != null && now.isAfter(expiresAt)) {
            throw IllegalStateException("쿠폰 사용 기간이 만료되었습니다. 만료일: $expiresAt, 현재일: $now")
        }

        return copy(isUsed = true, usedAt = now)
    }

    fun validateOwnership(userId: String) {
        if (this.userId != userId) {
            throw IllegalStateException("쿠폰 사용자가 일치하지 않습니다. 쿠폰 사용자: $userId, 요청 사용자: ${this.userId}")
        }
    }

    companion object {

        fun issue(
            userId: String,
            couponTemplateId: Long,
            expiresAt: LocalDateTime?,
            now: LocalDateTime,
        ): IssuedCoupon {
            return IssuedCoupon(
                id = UUID.randomUUID().toString().replace("-", ""),
                couponTemplateId = couponTemplateId,
                userId = userId,
                issuedAt = now,
                expiresAt = expiresAt,
                isUsed = false,
                usedAt = null,
            )
        }

        fun reconstitute(
            id: String,
            couponTemplateId: Long,
            userId: String,
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
