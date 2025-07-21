package com.goodpon.application.partner.coupon.service

import com.goodpon.application.partner.coupon.port.`in`.IssueCouponUseCase
import com.goodpon.application.partner.coupon.port.`in`.dto.IssueCouponCommand
import com.goodpon.application.partner.coupon.port.`in`.dto.IssueCouponResult
import com.goodpon.application.partner.coupon.service.accessor.CouponHistoryAccessor
import com.goodpon.application.partner.coupon.service.accessor.CouponTemplateAccessor
import com.goodpon.application.partner.coupon.service.accessor.CouponTemplateStatsAccessor
import com.goodpon.application.partner.coupon.service.accessor.UserCouponAccessor
import com.goodpon.application.partner.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.application.partner.coupon.service.exception.UserCouponAlreadyIssuedException
import com.goodpon.domain.coupon.service.CouponIssuer
import com.goodpon.domain.coupon.template.CouponTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class IssueCouponService(
    private val couponTemplateAccessor: CouponTemplateAccessor,
    private val couponTemplateStatsAccessor: CouponTemplateStatsAccessor,
    private val couponHistoryAccessor: CouponHistoryAccessor,
    private val userCouponAccessor: UserCouponAccessor,
) : IssueCouponUseCase {

    @Transactional
    override fun invoke(command: IssueCouponCommand): IssueCouponResult {
        val now = LocalDateTime.now()

        val stats = couponTemplateStatsAccessor.readByCouponTemplateIdForUpdate(command.couponTemplateId)
        val couponTemplate = couponTemplateAccessor.readById(command.couponTemplateId)

        validateCouponTemplateOwnership(couponTemplate, command.merchantId)
        validateAlreadyIssued(command.userId, command.couponTemplateId)

        val userCoupon = CouponIssuer.issue(
            couponTemplate = couponTemplate,
            userId = command.userId,
            currentIssueCount = stats.issueCount,
            issueAt = now
        )
        val savedUserCoupon = userCouponAccessor.createUserCoupon(userCoupon)

        couponHistoryAccessor.recordIssued(userCouponId = savedUserCoupon.id, recordedAt = now)
        couponTemplateStatsAccessor.incrementIssueCount(stats)

        return IssueCouponResult(
            userCouponId = savedUserCoupon.id,
            userId = savedUserCoupon.userId,
            couponTemplateId = couponTemplate.id,
            couponTemplateName = couponTemplate.name,
            issuedAt = now,
            expiresAt = savedUserCoupon.expiresAt,
        )
    }

    private fun validateCouponTemplateOwnership(couponTemplate: CouponTemplate, merchantId: Long) {
        if (!couponTemplate.isOwnedBy(merchantId)) {
            throw CouponTemplateNotOwnedByMerchantException()
        }
    }

    private fun validateAlreadyIssued(userId: String, couponTemplateId: Long) {
        if (userCouponAccessor.existsByUserIdAndCouponTemplateId(userId, couponTemplateId)) {
            throw UserCouponAlreadyIssuedException()
        }
    }
}