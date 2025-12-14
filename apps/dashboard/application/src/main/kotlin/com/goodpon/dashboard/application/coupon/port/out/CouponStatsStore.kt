package com.goodpon.dashboard.application.coupon.port.out

import java.time.Duration
import java.time.LocalDateTime

interface CouponStatsStore {

    fun initializeStats(couponTemplateId: Long, expiresAt: LocalDateTime? = null)

    fun readAllStats(): Map<Long, Pair<Long, Long>>

    /**
     * @return Map<couponTemplateId, Set<userId>>
     */
    fun getAllStaleCouponIssueReservations(olderThan: Duration): Map<Long, Set<String>>
}