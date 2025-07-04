package com.goodpon.core.application.coupon.accessor

import com.goodpon.core.domain.coupon.stats.CouponTemplateStats
import com.goodpon.core.domain.coupon.stats.CouponTemplateStatsRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CouponTemplateStatsStore(
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