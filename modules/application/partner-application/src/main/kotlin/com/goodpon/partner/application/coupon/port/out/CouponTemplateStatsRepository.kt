package com.goodpon.partner.application.coupon.port.out

import com.goodpon.domain.coupon.stats.CouponTemplateStats

interface CouponTemplateStatsRepository {

    fun save(couponTemplateStats: CouponTemplateStats): CouponTemplateStats

    fun findByCouponTemplateIdForUpdate(couponTemplateId: Long): CouponTemplateStats?
}