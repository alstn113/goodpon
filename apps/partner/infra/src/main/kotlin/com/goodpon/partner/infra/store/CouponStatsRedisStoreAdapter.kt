package com.goodpon.partner.infra.store

import com.goodpon.infra.redis.coupon.CouponStatsRedisStore
import com.goodpon.partner.application.coupon.port.out.CouponStatsStore
import org.springframework.stereotype.Component

@Component
class CouponStatsRedisStoreAdapter(
    private val couponStatsStore: CouponStatsRedisStore,
) : CouponStatsStore {

    override fun getStats(couponTemplateId: Long): Pair<Long, Long> {
        return couponStatsStore.getStats(couponTemplateId)
    }

    override fun getMultipleStats(couponTemplateIds: List<Long>): Map<Long, Pair<Long, Long>> {
        return couponStatsStore.getMultipleStats(couponTemplateIds)
    }
}