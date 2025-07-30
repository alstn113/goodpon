package com.goodpon.application.dashboard.coupon.port.out

import java.time.LocalDateTime

interface CouponTemplateStatsPort {

    fun initializeStats(couponTemplateId: Long, expiresAt: LocalDateTime? = null)
}