package com.goodpon.application.partner.coupon.service

import com.goodpon.application.partner.coupon.port.`in`.IssueCouponUseCase
import com.goodpon.application.partner.coupon.port.`in`.dto.IssueCouponCommand
import com.goodpon.application.partner.coupon.port.out.CouponEventPublisher
import com.goodpon.application.partner.coupon.port.out.CouponTemplateStatsCache
import com.goodpon.application.partner.coupon.port.out.dto.IssueCouponRequestedEvent
import com.goodpon.application.partner.coupon.port.out.dto.IssueResult
import com.goodpon.application.partner.coupon.service.accessor.CouponTemplateAccessor
import com.goodpon.application.partner.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.application.partner.coupon.service.exception.UserCouponAlreadyIssuedException
import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.template.exception.CouponTemplateIssuanceLimitExceededException
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class IssueCouponService(
    private val couponTemplateAccessor: CouponTemplateAccessor,
    private val couponTemplateStatsCache: CouponTemplateStatsCache,
    private val couponEventPublisher: CouponEventPublisher,
) : IssueCouponUseCase {

    override fun invoke(command: IssueCouponCommand) {
        val now = LocalDateTime.now()

        val couponTemplate = couponTemplateAccessor.readById(command.couponTemplateId)
        validateCouponTemplateOwnership(couponTemplate, command.merchantId)
        couponTemplate.validateIssue(now)

        handleIssueFailure(command = command, couponTemplate = couponTemplate)

        val event = IssueCouponRequestedEvent(
            couponTemplateId = couponTemplate.id,
            userId = command.userId,
            requestedAt = now
        )
        couponEventPublisher.publishIssueCouponRequested(event)
    }

    private fun handleIssueFailure(command: IssueCouponCommand, couponTemplate: CouponTemplate) {
        val result = couponTemplateStatsCache.reserveCoupon(
            couponTemplateId = couponTemplate.id,
            userId = command.userId,
            maxIssueCount = couponTemplate.maxIssueCount()
        )
        if (result == IssueResult.ALREADY_ISSUED) {
            throw UserCouponAlreadyIssuedException()
        }
        if (result == IssueResult.ISSUE_LIMIT_EXCEEDED) {
            throw CouponTemplateIssuanceLimitExceededException()
        }
    }

    private fun validateCouponTemplateOwnership(couponTemplate: CouponTemplate, merchantId: Long) {
        if (!couponTemplate.isOwnedBy(merchantId)) {
            throw CouponTemplateNotOwnedByMerchantException()
        }
    }
}