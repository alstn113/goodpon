package com.goodpon.core.application.coupon

import com.goodpon.core.application.coupon.accessor.UserCouponStore
import com.goodpon.core.application.coupon.event.CouponIssuedEvent
import com.goodpon.core.application.coupon.response.CouponIssueResultResponse
import com.goodpon.core.domain.coupon.template.CouponTemplate
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class CouponIssuer(
    private val userCouponStore: UserCouponStore,
    private val eventPublisher: ApplicationEventPublisher,
) {

    @Transactional
    fun issueCoupon(
        couponTemplate: CouponTemplate,
        userId: String,
        currentIssueCount: Long,
        issueAt: LocalDateTime,
    ): CouponIssueResultResponse {
        couponTemplate.validateIssue(currentIssuedCount = currentIssueCount, issueAt = issueAt)

        val userCoupon = userCouponStore.issueUserCoupon(
            userId = userId,
            couponTemplateId = couponTemplate.id,
            issueAt = issueAt,
            expiresAt = couponTemplate.calculateExpiresAt(issueAt.toLocalDate())
        )

        val event = CouponIssuedEvent(
            userCouponId = userCoupon.id,
            couponTemplateId = couponTemplate.id,
            userId = userCoupon.userId,
            issuedAt = userCoupon.issuedAt
        )
        eventPublisher.publishEvent(event)

        return CouponIssueResultResponse(
            userCouponId = userCoupon.id,
            userId = userCoupon.userId,
            couponTemplateId = userCoupon.couponTemplateId,
            couponTemplateName = couponTemplate.name,
            issuedAt = userCoupon.issuedAt,
            expiresAt = userCoupon.expiresAt
        )
    }
}
