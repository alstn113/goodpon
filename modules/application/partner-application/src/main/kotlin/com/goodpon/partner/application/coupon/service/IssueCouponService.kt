package com.goodpon.partner.application.coupon.service

import com.goodpon.domain.coupon.service.CouponIssuer
import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.partner.application.coupon.port.`in`.IssueCouponUseCase
import com.goodpon.partner.application.coupon.port.`in`.dto.IssueCouponCommand
import com.goodpon.partner.application.coupon.port.`in`.dto.IssueCouponResult
import com.goodpon.partner.application.coupon.service.accessor.*
import com.goodpon.partner.application.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.partner.application.coupon.service.exception.UserCouponAlreadyIssuedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class IssueCouponService(
    private val couponTemplateReader: CouponTemplateReader,
    private val couponTemplateStatsReader: CouponTemplateStatsReader,
    private val couponHistoryStore: CouponHistoryStore,
    private val userCouponStore: UserCouponStore,
    private val couponTemplateStatsStore: CouponTemplateStatsStore,
    private val userCouponReader: UserCouponReader,
) : IssueCouponUseCase {

    @Transactional
    override fun issueCoupon(command: IssueCouponCommand): IssueCouponResult {
        val now = LocalDateTime.now()

        val stats = couponTemplateStatsReader.readByCouponTemplateIdForUpdate(command.couponTemplateId)
        val couponTemplate = couponTemplateReader.readById(command.couponTemplateId)

        validateCouponTemplateOwnership(couponTemplate, command.merchantId)
        validateAlreadyIssued(command.userId, command.couponTemplateId)

        val userCoupon = CouponIssuer.issue(
            couponTemplate = couponTemplate,
            userId = command.userId,
            currentIssueCount = stats.issueCount,
            issueAt = now
        )
        userCouponStore.createUserCoupon(userCoupon)

        couponHistoryStore.recordIssued(userCouponId = userCoupon.id, recordedAt = now)
        couponTemplateStatsStore.incrementIssueCount(stats)

        return IssueCouponResult(
            userCouponId = userCoupon.id,
            userId = userCoupon.userId,
            couponTemplateId = couponTemplate.id,
            couponTemplateName = couponTemplate.name,
            issuedAt = userCoupon.issuedAt,
            expiresAt = userCoupon.expiresAt
        )
    }

    private fun validateCouponTemplateOwnership(
        couponTemplate: CouponTemplate,
        merchantId: Long,
    ) {
        if (!couponTemplate.isOwnedBy(merchantId)) {
            throw CouponTemplateNotOwnedByMerchantException()
        }
    }

    private fun validateAlreadyIssued(userId: String, couponTemplateId: Long) {
        if (userCouponReader.existsByUserIdAndCouponTemplateId(userId, couponTemplateId)) {
            throw UserCouponAlreadyIssuedException()
        }
    }
}