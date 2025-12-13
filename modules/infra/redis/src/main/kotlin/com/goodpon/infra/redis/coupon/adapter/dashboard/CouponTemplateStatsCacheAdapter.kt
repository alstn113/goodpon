package com.goodpon.infra.redis.coupon.adapter.dashboard

import com.goodpon.application.dashboard.coupon.port.out.CouponTemplateStatsCache
import com.goodpon.infra.redis.coupon.core.CouponTemplateStatsRedisCommandCache
import com.goodpon.infra.redis.coupon.core.CouponTemplateStatsRedisQueryCache
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDateTime

@Component("dashboardCouponTemplateStatsCacheAdapter")
class CouponTemplateStatsCacheAdapter(
    private val commandCache: CouponTemplateStatsRedisCommandCache,
    private val queryCache: CouponTemplateStatsRedisQueryCache,
) : CouponTemplateStatsCache {

    override fun initializeStats(couponTemplateId: Long, expiresAt: LocalDateTime?) {
        commandCache.initializeStats(couponTemplateId = couponTemplateId, expiresAt = expiresAt)
    }

    override fun readAllStats(): Map<Long, Pair<Long, Long>> {
        return queryCache.readAllStats()
    }

    override fun getAllStaleCouponIssueReservations(olderThan: Duration): Map<Long, Set<String>> {
        return queryCache.getAllStaleCouponIssueReservations(olderThan = olderThan)
    }
}