package com.goodpon.core.domain.coupon

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CouponTemplateStatsUpdater(
    private val couponTemplateStatsRepository: CouponTemplateStatsRepository,
) {

    @Transactional
    fun incrementIssueCount(couponTemplateId: Long): CouponTemplateStats {
        val stats = couponTemplateStatsRepository.findByCouponTemplateIdWithLock(couponTemplateId)
            ?: throw IllegalArgumentException("Coupon template stats not found for ID: $couponTemplateId")
        val updatedStats = stats.incrementIssueCount()
        return couponTemplateStatsRepository.save(updatedStats)
    }

    @Transactional
    fun incrementUsageCount(couponTemplateId: Long): CouponTemplateStats {
        val stats = couponTemplateStatsRepository.findByCouponTemplateIdWithLock(couponTemplateId)
            ?: throw IllegalArgumentException("Coupon template stats not found for ID: $couponTemplateId")
        val updatedStats = stats.incrementUsageCount()
        return couponTemplateStatsRepository.save(updatedStats)
    }
}