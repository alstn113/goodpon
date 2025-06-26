package com.goodpon.core.domain.coupon

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CouponTemplateStatsUpdater(
    private val couponTemplateStatsRepository: CouponTemplateStatsRepository,
) {

    @Transactional
    fun incrementIssueCount(stats: CouponTemplateStats): CouponTemplateStats {
        val updatedStats = stats.incrementIssueCount()
        return couponTemplateStatsRepository.save(updatedStats)
    }

    @Transactional
    fun incrementRedeemCount(stats: CouponTemplateStats): CouponTemplateStats {
        val updatedStats = stats.incrementRedeemCount()
        return couponTemplateStatsRepository.save(updatedStats)
    }

    @Transactional
    fun decrementRedeemCount(stats: CouponTemplateStats): CouponTemplateStats {
        val updatedStats = stats.decrementRedeemCount()
        return couponTemplateStatsRepository.save(updatedStats)
    }
}