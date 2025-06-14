package com.goodpon.common.domain.coupon

import java.time.LocalDateTime

data class IssuedCoupon(
    val id: String?,
    val couponTemplate: CouponTemplate,
    val accountId: String,
    val issuedAt: LocalDateTime,
    val expiresAt: LocalDateTime?,
    val isUsed: Boolean,
    val usedAt: LocalDateTime?,
    val updatedAt: LocalDateTime,
) {

    companion object {

        fun issue(
            accountId: String,
            couponTemplate: CouponTemplate,
            now: LocalDateTime = LocalDateTime.now(),
        ): IssuedCoupon {

            return IssuedCoupon(
                id = null,
                couponTemplate = couponTemplate.issue(now),
                accountId = accountId,
                issuedAt = now,
                expiresAt = couponTemplate.calculateFinalUseEndAt(now.toLocalDate()),
                isUsed = false,
                usedAt = null,
                updatedAt = now
            )
        }
    }

    fun use(now: LocalDateTime = LocalDateTime.now()): IssuedCoupon {
        if (isUsed) {
            throw IllegalStateException("이미 사용된 쿠폰입니다.")
        }

        return copy(
            isUsed = true,
            usedAt = now,
            updatedAt = now,
            couponTemplate = couponTemplate.use()
        )
    }
}
