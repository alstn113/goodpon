package com.goodpon.infra.db.jpa.adapter.partner

import com.goodpon.domain.coupon.stats.CouponTemplateStats
import com.goodpon.infra.db.jpa.core.CouponTemplateStatsCoreRepository
import com.goodpon.partner.application.coupon.port.out.CouponTemplateStatsRepository
import org.springframework.stereotype.Repository

@Repository("partnerCouponTemplateStatsJpaAdapter")
class CouponTemplateStatsJpaAdapter(
    private val couponTemplateStatsCoreRepository: CouponTemplateStatsCoreRepository,
) : CouponTemplateStatsRepository {

    override fun save(couponTemplateStats: CouponTemplateStats): CouponTemplateStats {
        return couponTemplateStatsCoreRepository.save(couponTemplateStats)
    }

    override fun findByCouponTemplateIdForUpdate(couponTemplateId: Long): CouponTemplateStats? {
        return couponTemplateStatsCoreRepository.findByCouponTemplateIdForUpdate(couponTemplateId)
    }
}
