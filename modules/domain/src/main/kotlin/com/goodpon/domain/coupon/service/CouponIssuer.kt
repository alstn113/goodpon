package com.goodpon.domain.coupon.service

import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.user.UserCoupon
import java.time.LocalDateTime

object CouponIssuer {

    fun issue(
        couponTemplate: CouponTemplate,
        userId: String,
        issueAt: LocalDateTime,
    ): UserCoupon {
        couponTemplate.validateIssue(issueAt = issueAt)

        val expiresAt = couponTemplate.calculateExpiresAt(issueAt.toLocalDate())

        return UserCoupon.issue(
            userId = userId,
            couponTemplateId = couponTemplate.id,
            expiresAt = expiresAt,
            issueAt = issueAt
        )
    }
}