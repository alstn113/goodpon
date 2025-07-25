package com.goodpon.domain.coupon.user

import com.goodpon.domain.coupon.user.exception.*
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
        if (status == UserCouponStatus.REDEEMED) {
            throw UserCouponAlreadyRedeemedException()
        }

        if (status != UserCouponStatus.ISSUED) {
            throw UserCouponRedeemNotAllowedException()
        }

        if (hasExpired(redeemAt)) {
            throw UserCouponExpiredException()
        }

        return copy(redeemedAt = redeemAt, status = UserCouponStatus.REDEEMED)
    }

    fun hasExpired(now: LocalDateTime): Boolean {
        if (expiresAt == null) {
            return false
        }
        return expiresAt <= now
    }

    fun cancelRedemption(): UserCoupon {
        if (status != UserCouponStatus.REDEEMED) {
            throw UserCouponCancelNotAllowedException()
        }

        return copy(redeemedAt = null, status = UserCouponStatus.ISSUED)
    }

    fun isRedeemed(): Boolean {
        return status == UserCouponStatus.REDEEMED
    }

    fun expire(): UserCoupon {
        if (status != UserCouponStatus.ISSUED) {
            throw UserCouponExpireNotAllowedException()
        }

        return this.copy(status = UserCouponStatus.EXPIRED)
    }

    fun isOwnedBy(userId: String): Boolean {
        return this.userId == userId
    }

    companion object {
        fun issue(
            userId: String,
            couponTemplateId: Long,
            expiresAt: LocalDateTime?,
            issueAt: LocalDateTime,
        ): UserCoupon {
            return UserCoupon(
                id = generateId(),
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

        private fun generateId(): String {
            return UUID.randomUUID().toString().replace("-", "")
        }
    }
}
