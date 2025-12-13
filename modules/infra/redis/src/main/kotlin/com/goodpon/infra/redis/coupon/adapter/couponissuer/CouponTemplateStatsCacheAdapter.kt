package com.goodpon.infra.redis.coupon.adapter.couponissuer

import com.goodpon.application.couponissuer.port.out.CouponTemplateStatsCache
import com.goodpon.infra.redis.coupon.core.CouponTemplateStatsRedisCommandCache
import org.springframework.stereotype.Component

@Component("couponIssuerCouponTemplateStatsCacheAdapter")
class CouponTemplateStatsCacheAdapter(
    private val commandCache: CouponTemplateStatsRedisCommandCache,
) : CouponTemplateStatsCache {

    override fun hasValidReservation(couponTemplateId: Long, userId: String): Boolean {
        return commandCache.hasValidReservation(couponTemplateId = couponTemplateId, userId = userId)
    }

    override fun completeIssueCoupon(couponTemplateId: Long, userId: String) {
        commandCache.completeIssueCoupon(couponTemplateId = couponTemplateId, userId = userId)
    }

    override fun markAsFailedIssuance(couponTemplateId: Long, userId: String) {
        commandCache.markAsFailedIssuance(couponTemplateId = couponTemplateId, userId = userId)
    }

    override fun cancelIssue(couponTemplateId: Long, userId: String) {
        commandCache.cancelIssue(couponTemplateId = couponTemplateId, userId = userId)
    }
}