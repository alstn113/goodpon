package com.goodpon.core.domain.coupon.service

import com.goodpon.core.domain.coupon.history.CouponHistory
import com.goodpon.core.domain.coupon.history.CouponHistoryRepository
import com.goodpon.core.domain.coupon.template.CouponTemplate
import com.goodpon.core.domain.coupon.user.UserCoupon
import com.goodpon.core.domain.coupon.user.UserCouponRepository
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
        couponTemplate.validateIssue(currentIssuedCount = issueCount, now = now)
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
