package com.goodpon.core.application.coupon.accessor

import com.goodpon.core.application.coupon.exception.CouponTemplateStatsNotFoundException
import com.goodpon.core.domain.coupon.stats.CouponTemplateStats
import com.goodpon.core.domain.coupon.stats.CouponTemplateStatsRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CouponTemplateStatsReader(
    private val couponTemplateStatsRepository: CouponTemplateStatsRepository,
) {
    @Transactional(readOnly = true)
    fun readByCouponTemplateIdForUpdate(couponTemplateId: Long): CouponTemplateStats {
        return couponTemplateStatsRepository.findByCouponTemplateIdForUpdate(couponTemplateId)
            ?: throw CouponTemplateStatsNotFoundException()
    }
}