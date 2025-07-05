package com.goodpon.core.application.coupon

import com.goodpon.core.application.coupon.accessor.*
import com.goodpon.core.application.coupon.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.core.application.coupon.exception.UserCouponAlreadyIssuedException
import com.goodpon.core.application.coupon.request.IssueCouponRequest
import com.goodpon.core.application.coupon.response.CouponIssueResultResponse
import com.goodpon.core.domain.coupon.template.CouponTemplate
import com.goodpon.core.domain.coupon.template.service.CouponIssuer
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime


@Service
class CouponIssueService(
    private val couponTemplateReader: CouponTemplateReader,
    private val couponTemplateStatsReader: CouponTemplateStatsReader,
    private val couponHistoryStore: CouponHistoryStore,
    private val userCouponStore: UserCouponStore,
    private val couponTemplateStatsStore: CouponTemplateStatsStore,
    private val userCouponReader: UserCouponReader,
) {

    @Transactional
    fun issueCoupon(request: IssueCouponRequest): CouponIssueResultResponse {
        val now = LocalDateTime.now()

        val stats = couponTemplateStatsReader.readByCouponTemplateIdForUpdate(request.couponTemplateId)
        val couponTemplate = couponTemplateReader.readByIdForRead(request.couponTemplateId)

        validateCouponTemplateOwnership(couponTemplate, request.merchantPrincipal.merchantId)
        validateAlreadyIssued(request.userId, request.couponTemplateId)

        val userCoupon = CouponIssuer.issue(
            couponTemplate = couponTemplate,
            userId = request.userId,
            currentIssueCount = stats.issueCount,
            issueAt = now
        )
        userCouponStore.createUserCoupon(userCoupon)

        couponHistoryStore.recordIssued(userCouponId = userCoupon.id, recordedAt = now)
        couponTemplateStatsStore.incrementIssueCount(stats)

        return CouponIssueResultResponse(
            userCouponId = userCoupon.id,
            userId = userCoupon.userId,
            couponTemplateId = couponTemplate.id,
            couponTemplateName = couponTemplate.name,
            issuedAt = userCoupon.issuedAt,
            expiresAt = userCoupon.expiresAt
        )
    }

    private fun validateCouponTemplateOwnership(couponTemplate: CouponTemplate, merchantId: Long) {
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