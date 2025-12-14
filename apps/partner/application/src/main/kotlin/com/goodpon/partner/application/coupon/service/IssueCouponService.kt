package com.goodpon.partner.application.coupon.service

import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.template.exception.CouponTemplateIssuanceLimitExceededException
import com.goodpon.partner.application.coupon.port.`in`.IssueCouponUseCase
import com.goodpon.partner.application.coupon.port.`in`.dto.IssueCouponCommand
import com.goodpon.partner.application.coupon.port.out.CouponEventPublisher
import com.goodpon.partner.application.coupon.port.out.CouponIssueStore
import com.goodpon.partner.application.coupon.port.out.dto.IssueCouponRequestedEvent
import com.goodpon.partner.application.coupon.port.out.dto.IssueResult
import com.goodpon.partner.application.coupon.service.accessor.CouponTemplateAccessor
import com.goodpon.partner.application.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.partner.application.coupon.service.exception.UserCouponAlreadyIssuedException
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class IssueCouponService(
    private val couponTemplateAccessor: CouponTemplateAccessor,
    private val couponIssueStore: CouponIssueStore,
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
        val result = couponIssueStore.reserveCoupon(
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