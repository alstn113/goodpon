package com.goodpon.infra.db.jpa.adapter.dashboard

import com.goodpon.dashboard.application.coupon.port.out.CouponTemplateStatsRepository
import com.goodpon.domain.coupon.stats.CouponTemplateStats
import com.goodpon.infra.db.jpa.core.CouponTemplateStatsCoreRepository
import org.springframework.stereotype.Repository

@Repository("dashboardCouponTemplateStatsJpaAdapter")
class CouponTemplateStatsJpaAdapter(
    private val couponTemplateStatsCoreRepository: CouponTemplateStatsCoreRepository,
) : CouponTemplateStatsRepository {

    override fun save(couponTemplateStats: CouponTemplateStats): CouponTemplateStats {
        return couponTemplateStatsCoreRepository.save(couponTemplateStats)
    }
}