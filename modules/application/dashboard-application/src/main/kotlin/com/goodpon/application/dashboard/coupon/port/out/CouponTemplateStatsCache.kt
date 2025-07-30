package com.goodpon.application.dashboard.coupon.port.out

import java.time.LocalDateTime

interface CouponTemplateStatsCache {

    fun initializeStats(couponTemplateId: Long, expiresAt: LocalDateTime? = null)
}