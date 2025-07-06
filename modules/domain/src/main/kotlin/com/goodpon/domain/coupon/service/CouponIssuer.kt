package com.goodpon.domain.coupon.service

import java.time.LocalDateTime

object CouponIssuer {

    fun issue(
        couponTemplate: com.goodpon.domain.coupon.template.CouponTemplate,
        currentIssueCount: Long,
        userId: String,
        issueAt: LocalDateTime,
    ): com.goodpon.domain.coupon.user.UserCoupon {
        couponTemplate.validateIssue(currentIssuedCount = currentIssueCount, issueAt = issueAt)

        val expiresAt = couponTemplate.calculateExpiresAt(issueAt.toLocalDate())

        return com.goodpon.domain.coupon.user.UserCoupon.issue(
            userId = userId,
            couponTemplateId = couponTemplate.id,
            expiresAt = expiresAt,
            issueAt = issueAt
        )
    }
}