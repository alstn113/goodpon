package com.goodpon.core.domain.coupon

import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class CouponIssuer(
    private val issuedCouponRepository: UserCouponRepository,
) {

    fun issueCoupon(
        couponTemplate: CouponTemplate,
        userId: String,
        issueCount: Long,
        now: LocalDateTime = LocalDateTime.now(),
    ): CouponIssueResult {

        couponTemplate.validateIssue(issueCount = issueCount, now = now)
            .onFailure { throw it }

        val issuedCoupon = UserCoupon.issue(
            userId = userId,
            couponTemplateId = couponTemplate.id,
            expiresAt = couponTemplate.calculateFinalUsageEndAt(now.toLocalDate()),
            now = now
        )
        val savedUserCoupon = issuedCouponRepository.save(issuedCoupon)

        return CouponIssueResult(
            issuedCouponId = savedUserCoupon.id,
            userId = savedUserCoupon.userId,
            couponTemplateId = savedUserCoupon.couponTemplateId,
            couponTemplateName = couponTemplate.name,
            issuedAt = savedUserCoupon.issuedAt,
            expiresAt = savedUserCoupon.expiresAt
        )
    }
}
