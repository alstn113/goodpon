package com.goodpon.domain.application.coupon.accessor

import com.goodpon.domain.application.coupon.exception.CouponTemplateStatsNotFoundException
import com.goodpon.domain.domain.coupon.stats.CouponTemplateStats
import com.goodpon.domain.domain.coupon.stats.CouponTemplateStatsRepository
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