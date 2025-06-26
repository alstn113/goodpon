package com.goodpon.core.domain.coupon

interface CouponTemplateStatsRepository {

    fun save(couponTemplateStats: CouponTemplateStats): CouponTemplateStats
    fun findByCouponTemplateIdWithLock(couponTemplateId: Long): CouponTemplateStats?
}