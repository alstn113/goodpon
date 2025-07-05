package com.goodpon.domain.coupon.user

import com.goodpon.domain.domain.coupon.user.exception.UserCouponCancelNotAllowedException
import com.goodpon.domain.domain.coupon.user.exception.UserCouponExpireNotAllowedException
import com.goodpon.domain.coupon.user.exception.UserCouponExpiredException
import com.goodpon.domain.coupon.user.exception.UserCouponRedeemNotAllowedException
import java.time.LocalDateTime
import java.util.*

data class UserCoupon private constructor(
    val id: String,
    val couponTemplateId: Long,
    val userId: String,
    val status: com.goodpon.domain.coupon.user.UserCouponStatus,
    val issuedAt: LocalDateTime,
    val redeemedAt: LocalDateTime?,
    val expiresAt: LocalDateTime?,
) {

    fun redeem(redeemAt: LocalDateTime): com.goodpon.domain.coupon.user.UserCoupon {
        if (status != com.goodpon.domain.coupon.user.UserCouponStatus.ISSUED) {
            throw com.goodpon.domain.coupon.user.exception.UserCouponRedeemNotAllowedException()
        }

        if (hasExpired(redeemAt)) {
            throw com.goodpon.domain.coupon.user.exception.UserCouponExpiredException()
        }

        return copy(redeemedAt = redeemAt, status = com.goodpon.domain.coupon.user.UserCouponStatus.REDEEMED)
    }

    fun hasExpired(now: LocalDateTime): Boolean {
        if (expiresAt == null) {
            return false
        }
        return expiresAt <= now
    }

    fun cancelRedemption(): com.goodpon.domain.coupon.user.UserCoupon {
        if (status != com.goodpon.domain.coupon.user.UserCouponStatus.REDEEMED) {
            throw UserCouponCancelNotAllowedException()
        }

        return copy(redeemedAt = null, status = com.goodpon.domain.coupon.user.UserCouponStatus.ISSUED)
    }

    fun expire(): com.goodpon.domain.coupon.user.UserCoupon {
        if (status != com.goodpon.domain.coupon.user.UserCouponStatus.ISSUED) {
            throw UserCouponExpireNotAllowedException()
        }

        return this.copy(status = com.goodpon.domain.coupon.user.UserCouponStatus.EXPIRED)
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
        ): com.goodpon.domain.coupon.user.UserCoupon {
            return com.goodpon.domain.coupon.user.UserCoupon(
                id = com.goodpon.domain.coupon.user.UserCoupon.Companion.generateId(),
                couponTemplateId = couponTemplateId,
                userId = userId,
                status = com.goodpon.domain.coupon.user.UserCouponStatus.ISSUED,
                issuedAt = issueAt,
                expiresAt = expiresAt,
                redeemedAt = null,
            )
        }

        fun reconstruct(
            id: String,
            couponTemplateId: Long,
            userId: String,
            status: com.goodpon.domain.coupon.user.UserCouponStatus,
            issuedAt: LocalDateTime,
            expiresAt: LocalDateTime?,
            redeemedAt: LocalDateTime?,
        ): com.goodpon.domain.coupon.user.UserCoupon {
            return com.goodpon.domain.coupon.user.UserCoupon(
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
