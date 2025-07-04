package com.goodpon.core.application.coupon

import com.goodpon.core.domain.coupon.template.CouponTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class CouponIssuer(
    private val userCouponAppender: UserCouponAppender,
    private val couponHistoryRecorder: CouponHistoryRecorder,
) {
    @Transactional
    fun issueCoupon(
        couponTemplate: CouponTemplate,
        userId: String,
        issueCount: Long,
        issueAt: LocalDateTime,
    ): CouponIssueResult {
        couponTemplate.validateIssue(currentIssuedCount = issueCount, issueAt = issueAt)

        val userCoupon = userCouponAppender.append(
            userId = userId,
            couponTemplateId = couponTemplate.id,
            issueAt = issueAt,
            expiresAt = couponTemplate.calculateExpiresAt(issueAt.toLocalDate())
        )
        couponHistoryRecorder.recordIssued(
            userCouponId = userCoupon.id,
            recordedAt = issueAt
        )

        return CouponIssueResult(
            userCouponId = userCoupon.id,
            userId = userCoupon.userId,
            couponTemplateId = userCoupon.couponTemplateId,
            couponTemplateName = couponTemplate.name,
            issuedAt = userCoupon.issuedAt,
            expiresAt = userCoupon.expiresAt
        )
    }
}
