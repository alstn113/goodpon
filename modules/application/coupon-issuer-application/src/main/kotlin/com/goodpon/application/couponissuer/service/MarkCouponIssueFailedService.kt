package com.goodpon.application.couponissuer.service

import com.goodpon.application.couponissuer.port.`in`.MarkCouponIssueFailedUseCase
import com.goodpon.application.couponissuer.port.out.CouponTemplateStatsCache
import org.springframework.stereotype.Service

@Service
class MarkCouponIssueFailedService(
    private val couponTemplateStatsCache: CouponTemplateStatsCache,
) : MarkCouponIssueFailedUseCase {

    override fun invoke(couponTemplateId: Long, userId: String) {
        couponTemplateStatsCache.markAsFailedIssuance(couponTemplateId = couponTemplateId, userId = userId)
    }
}