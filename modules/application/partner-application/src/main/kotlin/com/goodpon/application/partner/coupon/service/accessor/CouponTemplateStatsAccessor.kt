package com.goodpon.application.partner.coupon.service.accessor

import com.goodpon.application.partner.coupon.port.out.CouponTemplateStatsRepository
import com.goodpon.application.partner.coupon.service.exception.CouponTemplateStatsNotFoundException
import com.goodpon.domain.coupon.stats.CouponTemplateStats
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CouponTemplateStatsAccessor(
    private val couponTemplateStatsRepository: CouponTemplateStatsRepository,
) {

    @Transactional(readOnly = true)
    fun readByCouponTemplateIdForUpdate(couponTemplateId: Long): CouponTemplateStats {
        return couponTemplateStatsRepository.findByCouponTemplateIdForUpdate(couponTemplateId)
            ?: throw CouponTemplateStatsNotFoundException()
    }

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