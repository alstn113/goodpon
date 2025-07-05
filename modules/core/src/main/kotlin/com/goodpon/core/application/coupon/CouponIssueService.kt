package com.goodpon.core.application.coupon

import com.goodpon.core.application.coupon.accessor.CouponTemplateReader
import com.goodpon.core.application.coupon.accessor.CouponTemplateStatsReader
import com.goodpon.core.application.coupon.accessor.UserCouponReader
import com.goodpon.core.application.coupon.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.core.application.coupon.exception.UserCouponAlreadyIssuedException
import com.goodpon.core.application.coupon.request.IssueCouponRequest
import com.goodpon.core.application.coupon.response.CouponIssueResultResponse
import com.goodpon.core.domain.coupon.template.CouponTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime


@Service
class CouponIssueService(
    private val couponTemplateReader: CouponTemplateReader,
    private val couponTemplateStatsReader: CouponTemplateStatsReader,
    private val userCouponReader: UserCouponReader,
    private val couponIssuer: CouponIssuer,
) {

    @Transactional
    fun issueCoupon(request: IssueCouponRequest): CouponIssueResultResponse {
        val now = LocalDateTime.now()

        val stats = couponTemplateStatsReader.readByCouponTemplateIdForUpdate(request.couponTemplateId)
        val couponTemplate = couponTemplateReader.readByIdForRead(request.couponTemplateId)

        validateCouponTemplateOwnership(couponTemplate, request.merchantPrincipal.merchantId)
        validateAlreadyIssued(request.userId, request.couponTemplateId)

        return couponIssuer.issueCoupon(
            couponTemplate = couponTemplate,
            userId = request.userId,
            currentIssueCount = stats.issueCount,
            issueAt = now
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