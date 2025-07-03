package com.goodpon.core.application.coupon

import com.goodpon.core.application.coupon.request.IssueCouponRequest
import com.goodpon.core.domain.coupon.service.CouponIssueResult
import com.goodpon.core.domain.coupon.service.CouponIssuer
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime


@Service
class CouponIssueService(
    private val couponTemplateReader: CouponTemplateReader,
    private val couponTemplateStatsReader: CouponTemplateStatsReader,
    private val userCouponReader: UserCouponReader,
    private val couponTemplateStatsUpdater: CouponTemplateStatsUpdater,
    private val couponIssuer: CouponIssuer,
) {
    @Transactional
    fun issueCoupon(request: IssueCouponRequest): CouponIssueResult {
        val stats = couponTemplateStatsReader.readByCouponTemplateIdForUpdate(request.couponTemplateId)
        val couponTemplate = couponTemplateReader.readByIdForRead(request.couponTemplateId)

        couponTemplate.validateOwnership(request.merchantPrincipal.merchantId)
        validateAlreadyIssued(userId = request.userId, couponTemplateId = request.couponTemplateId)

        val now = LocalDateTime.now()
        val couponIssueResult = couponIssuer.issueCoupon(
            couponTemplate = couponTemplate,
            userId = request.userId,
            issueCount = stats.issueCount,
            now = now
        )
        couponTemplateStatsUpdater.incrementIssueCount(stats)

        return couponIssueResult
    }

    private fun validateAlreadyIssued(userId: String, couponTemplateId: Long) {
        if (userCouponReader.existsByUserIdAndCouponTemplateId(userId, couponTemplateId)) {
            throw IllegalArgumentException("이미 발급된 쿠폰이 존재합니다.")
        }
    }
}