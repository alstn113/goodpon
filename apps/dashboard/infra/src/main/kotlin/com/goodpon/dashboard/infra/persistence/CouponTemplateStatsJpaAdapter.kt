package com.goodpon.dashboard.infra.persistence

import com.goodpon.dashboard.application.coupon.port.out.CouponTemplateStatsRepository
import com.goodpon.domain.coupon.stats.CouponTemplateStats
import com.goodpon.infra.jpa.core.CouponTemplateStatsCoreRepository
import org.springframework.stereotype.Repository

@Repository
class CouponTemplateStatsJpaAdapter(
    private val couponTemplateStatsCoreRepository: CouponTemplateStatsCoreRepository,
) : CouponTemplateStatsRepository {

    override fun save(couponTemplateStats: CouponTemplateStats): CouponTemplateStats {
        return couponTemplateStatsCoreRepository.save(couponTemplateStats)
    }

    override fun batchUpdateCouponTemplateStats(statsUpdates: Map<Long, Pair<Long, Long>>) {
        couponTemplateStatsCoreRepository.batchUpdateCouponTemplateStats(statsUpdates)
    }
}