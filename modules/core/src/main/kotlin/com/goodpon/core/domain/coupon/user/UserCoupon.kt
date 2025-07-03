package com.goodpon.core.domain.coupon.user

import java.time.LocalDateTime
import java.util.*

data class UserCoupon private constructor(
    val id: String,
    val couponTemplateId: Long,
    val userId: String,
    val status: UserCouponStatus,
    val issuedAt: LocalDateTime,
    val redeemedAt: LocalDateTime?,
    val expiresAt: LocalDateTime?,
) {
    fun redeem(redeemAt: LocalDateTime): UserCoupon {
        if (status != UserCouponStatus.ISSUED) {
            throw IllegalStateException("쿠폰을 사용할 수 있는 상태가 아닙니다. 현재 상태: $status")
        }

        if (isExpired(redeemAt)) {
            throw IllegalStateException("쿠폰 사용 기간이 만료되었습니다. 만료일: $expiresAt, 현재일: $redeemAt")
        }

        return copy(redeemedAt = redeemAt, status = UserCouponStatus.REDEEMED)
    }

    fun cancelRedemption(): UserCoupon {
        if (status != UserCouponStatus.REDEEMED) {
            throw IllegalStateException("쿠폰이 사용되지 않았거나 이미 취소되었습니다.")
        }

        return copy(redeemedAt = null, status = UserCouponStatus.ISSUED)
    }

    fun expire(): UserCoupon {
        if (status != UserCouponStatus.ISSUED) {
            throw IllegalStateException("쿠폰이 만료될 수 있는 상태가 아닙니다. 현재 상태: $status")
        }

        return this.copy(status = UserCouponStatus.EXPIRED)
    }

    fun isOwnedBy(userId: String): Boolean {
        return this.userId == userId
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
            issueAt: LocalDateTime,
        ): UserCoupon {
            return UserCoupon(
                id = UUID.randomUUID().toString().replace("-", ""),
                couponTemplateId = couponTemplateId,
                userId = userId,
                status = UserCouponStatus.ISSUED,
                issuedAt = issueAt,
                expiresAt = expiresAt,
                redeemedAt = null,
            )
        }

        fun reconstruct(
            id: String,
            couponTemplateId: Long,
            userId: String,
            status: UserCouponStatus,
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
