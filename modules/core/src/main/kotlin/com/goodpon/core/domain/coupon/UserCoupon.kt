package com.goodpon.core.domain.coupon

import java.time.LocalDateTime
import java.util.*

data class UserCoupon private constructor(
    val id: String,
    val couponTemplateId: Long,
    val userId: String,
    val status: CouponStatus,
    val issuedAt: LocalDateTime,
    val redeemedAt: LocalDateTime?,
    val expiresAt: LocalDateTime?,
) {

    fun redeem(now: LocalDateTime = LocalDateTime.now()): UserCoupon {
        if (status != CouponStatus.ISSUED) {
            throw IllegalStateException("쿠폰을 사용할 수 있는 상태가 아닙니다. 현재 상태: $status")
        }

        if (isExpired(now)) {
            throw IllegalStateException("쿠폰 사용 기간이 만료되었습니다. 만료일: $expiresAt, 현재일: $now")
        }

        return copy(redeemedAt = now, status = CouponStatus.REDEEMED)
    }

    fun cancelRedemption(): UserCoupon {
        if (status != CouponStatus.REDEEMED) {
            throw IllegalStateException("쿠폰이 사용되지 않았거나 이미 취소되었습니다.")
        }

        return copy(redeemedAt = null, status = CouponStatus.ISSUED)
    }

    fun expire(): UserCoupon {
        if (status != CouponStatus.ISSUED) {
            throw IllegalStateException("쿠폰이 만료될 수 있는 상태가 아닙니다. 현재 상태: $status")
        }

        return this.copy(status = CouponStatus.EXPIRED)
    }

    fun validateOwnership(userId: String) {
        if (this.userId != userId) {
            throw IllegalStateException("쿠폰 사용자가 일치하지 않습니다. 쿠폰 사용자: $userId, 요청 사용자: ${this.userId}")
        }
    }

    fun isExpired(now: LocalDateTime): Boolean {
        if (expiresAt == null) {
            return false
        }
        return expiresAt <= now
    }

    companion object {

        fun issue(
            userId: String,
            couponTemplateId: Long,
            expiresAt: LocalDateTime?,
            now: LocalDateTime,
        ): UserCoupon {
            return UserCoupon(
                id = UUID.randomUUID().toString().replace("-", ""),
                couponTemplateId = couponTemplateId,
                userId = userId,
                status = CouponStatus.ISSUED,
                issuedAt = now,
                expiresAt = expiresAt,
                redeemedAt = null,
            )
        }

        fun reconstitute(
            id: String,
            couponTemplateId: Long,
            userId: String,
            status: CouponStatus,
            issuedAt: LocalDateTime,
            expiresAt: LocalDateTime?,
            redeemedAt: LocalDateTime?,
        ): UserCoupon {
            return UserCoupon(
                id = id,
                couponTemplateId = couponTemplateId,
                userId = userId,
                status = status,
                issuedAt = issuedAt,
                expiresAt = expiresAt,
                redeemedAt = redeemedAt
            )
        }
    }
}
