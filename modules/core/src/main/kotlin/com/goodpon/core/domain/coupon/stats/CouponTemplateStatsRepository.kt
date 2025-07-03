package com.goodpon.core.domain.coupon.stats

interface CouponTemplateStatsRepository {
    fun save(couponTemplateStats: CouponTemplateStats): CouponTemplateStats
    fun findByCouponTemplateIdForUpdate(couponTemplateId: Long): CouponTemplateStats?
}