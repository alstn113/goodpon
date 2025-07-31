package com.goodpon.application.dashboard.coupon.service.accessor

import com.goodpon.application.dashboard.coupon.port.out.CouponTemplateStatsRepository
import com.goodpon.domain.coupon.stats.CouponTemplateStats
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CouponTemplateStatsAccessor(
    private val couponTemplateStatsRepository: CouponTemplateStatsRepository,
) {

    @Transactional
    fun save(couponTemplateStats: CouponTemplateStats): CouponTemplateStats {
        return couponTemplateStatsRepository.save(couponTemplateStats)
    }

    @Transactional
    fun batchUpdateCouponTemplateStats(statsUpdates: Map<Long, Pair<Long, Long>>) {
        couponTemplateStatsRepository.batchUpdateCouponTemplateStats(statsUpdates)
    }
}