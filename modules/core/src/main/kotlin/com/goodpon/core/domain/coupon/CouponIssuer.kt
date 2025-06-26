package com.goodpon.core.domain.coupon

import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class CouponIssuer(
    private val issuedCouponRepository: IssuedCouponRepository,
) {

    fun issueCoupon(
        couponTemplate: CouponTemplate,
        userId: String,
        issueCount: Long,
        now: LocalDateTime = LocalDateTime.now(),
    ): CouponIssueResult {

        couponTemplate.validateIssue(issueCount = issueCount, now = now)
            .onFailure { throw it }

        val issuedCoupon = IssuedCoupon.issue(
            userId = userId,
            couponTemplateId = couponTemplate.id,
            expiresAt = couponTemplate.calculateFinalUsageEndAt(now.toLocalDate()),
            now = now
        )
        val savedIssuedCoupon = issuedCouponRepository.save(issuedCoupon)

        return CouponIssueResult(
            issuedCouponId = savedIssuedCoupon.id,
            userId = savedIssuedCoupon.userId,
            couponTemplateId = savedIssuedCoupon.couponTemplateId,
            couponTemplateName = couponTemplate.name,
            issuedAt = savedIssuedCoupon.issuedAt,
            expiresAt = savedIssuedCoupon.expiresAt
        )
    }
}
