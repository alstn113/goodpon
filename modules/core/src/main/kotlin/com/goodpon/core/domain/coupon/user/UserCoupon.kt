package com.goodpon.core.domain.coupon.user

import com.goodpon.core.domain.coupon.user.exception.UserCouponCancelNotAllowedException
import com.goodpon.core.domain.coupon.user.exception.UserCouponExpireNotAllowedException
import com.goodpon.core.domain.coupon.user.exception.UserCouponExpiredException
import com.goodpon.core.domain.coupon.user.exception.UserCouponRedeemNotAllowedException
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
