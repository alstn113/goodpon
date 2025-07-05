package com.goodpon.core.domain.coupon.template.service

import com.goodpon.core.domain.coupon.template.CouponTemplate
import com.goodpon.core.domain.coupon.user.UserCoupon
import java.time.LocalDateTime

object CouponIssuer {

    fun issue(
        couponTemplate: CouponTemplate,
        currentIssueCount: Long,
        userId: String,
        issueAt: LocalDateTime,
    ): UserCoupon {
        couponTemplate.validateIssue(currentIssuedCount = currentIssueCount, issueAt = issueAt)

        val expiresAt = couponTemplate.calculateExpiresAt(issueAt.toLocalDate())

        return UserCoupon.issue(
            userId = userId,
            couponTemplateId = couponTemplate.id,
            expiresAt = expiresAt,
            issueAt = issueAt
        )
    }
}