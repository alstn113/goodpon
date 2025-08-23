package com.goodpon.infra.redis.coupon.adapter.couponissuer

import com.goodpon.application.couponissuer.port.out.CouponTemplateStatsCache
import com.goodpon.infra.redis.coupon.core.CouponTemplateStatsRedisCommandCache
import org.springframework.stereotype.Component

@Component("couponIssuerCouponTemplateStatsCacheAdapter")
class CouponTemplateStatsCacheAdapter(
    private val commandCache: CouponTemplateStatsRedisCommandCache,
) : CouponTemplateStatsCache {

    override fun cancelIssue(couponTemplateId: Long, userId: String) {
        commandCache.cancelIssue(couponTemplateId = couponTemplateId, userId = userId)
    }
}