package com.goodpon.partner.application.coupon.service.accessor

import com.goodpon.domain.coupon.stats.CouponTemplateStats
import com.goodpon.partner.application.coupon.port.out.CouponTemplateStatsRepository
import com.goodpon.partner.application.coupon.service.exception.CouponTemplateStatsNotFoundException
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