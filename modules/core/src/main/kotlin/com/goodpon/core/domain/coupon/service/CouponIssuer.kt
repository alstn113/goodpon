package com.goodpon.core.domain.coupon.service

import com.goodpon.core.domain.coupon.*
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class CouponIssuer(
    private val userCouponRepository: UserCouponRepository,
    private val couponHistoryRepository: CouponHistoryRepository,
) {
    @Transactional
    fun issueCoupon(
        couponTemplate: CouponTemplate,
        userId: String,
        issueCount: Long,
        now: LocalDateTime,
    ): CouponIssueResult {
        couponTemplate.validateIssue(issueCount = issueCount, now = now)
            .onFailure { throw it }

        val userCoupon = UserCoupon.issue(
            userId = userId,
            couponTemplateId = couponTemplate.id,
            expiresAt = couponTemplate.calculateExpiresAt(now.toLocalDate()),
            now = now
        )
        val savedUserCoupon = userCouponRepository.save(userCoupon)
        val history = CouponHistory.issued(userCouponId = savedUserCoupon.id, now = now)
        couponHistoryRepository.save(history)

        return CouponIssueResult(
            userCouponId = savedUserCoupon.id,
            userId = savedUserCoupon.userId,
            couponTemplateId = savedUserCoupon.couponTemplateId,
            couponTemplateName = couponTemplate.name,
            issuedAt = savedUserCoupon.issuedAt,
            expiresAt = savedUserCoupon.expiresAt
        )
    }
}
