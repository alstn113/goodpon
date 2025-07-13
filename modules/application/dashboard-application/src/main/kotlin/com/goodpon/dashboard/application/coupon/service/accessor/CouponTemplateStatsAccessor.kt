package com.goodpon.dashboard.application.coupon.service.accessor

import com.goodpon.dashboard.application.coupon.port.out.CouponTemplateStatsRepository
import com.goodpon.domain.coupon.stats.CouponTemplateStats
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CouponTemplateStatsAccessor(
    private val couponTemplateStatsRepository: CouponTemplateStatsRepository
) {

    @Transactional
    fun save(couponTemplateStats: CouponTemplateStats): CouponTemplateStats {
        return couponTemplateStatsRepository.save(couponTemplateStats)
    }
}