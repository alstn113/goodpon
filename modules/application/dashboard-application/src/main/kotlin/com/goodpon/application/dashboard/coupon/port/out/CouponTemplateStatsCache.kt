package com.goodpon.application.dashboard.coupon.port.out

import java.time.Duration
import java.time.LocalDateTime

interface CouponTemplateStatsCache {

    fun initializeStats(couponTemplateId: Long, expiresAt: LocalDateTime? = null)

    fun readAllStats(): Map<Long, Pair<Long, Long>>

    /**
     * @return Map<couponTemplateId, Set<userId>>
     */
    fun getAllStaleCouponIssueReservations(olderThan: Duration): Map<Long, Set<String>>
}