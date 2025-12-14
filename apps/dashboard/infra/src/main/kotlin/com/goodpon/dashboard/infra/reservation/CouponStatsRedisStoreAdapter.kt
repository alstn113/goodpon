package com.goodpon.dashboard.infra.reservation

import com.goodpon.dashboard.application.coupon.port.out.CouponStatsStore
import com.goodpon.infra.redis.coupon.CouponIssueRedisStore
import com.goodpon.infra.redis.coupon.CouponStatsRedisStore
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDateTime

@Component
class CouponStatsRedisStoreAdapter(
    private val couponIssueStore: CouponIssueRedisStore,
    private val couponStatsStore: CouponStatsRedisStore,
) : CouponStatsStore {

    override fun initializeStats(couponTemplateId: Long, expiresAt: LocalDateTime?) {
        couponIssueStore.initialize(couponTemplateId = couponTemplateId, expiresAt = expiresAt)
    }

    override fun readAllStats(): Map<Long, Pair<Long, Long>> {
        return couponStatsStore.getAllStats()
    }

    override fun getAllStaleCouponIssueReservations(olderThan: Duration): Map<Long, Set<String>> {
        return couponStatsStore.getAllStaleCouponIssueReservations(olderThan = olderThan)
    }
}