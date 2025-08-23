package com.goodpon.infra.redis.coupon.adapter.partner

import com.goodpon.application.partner.coupon.port.out.CouponTemplateStatsCache
import com.goodpon.application.partner.coupon.port.out.dto.IssueResult
import com.goodpon.application.partner.coupon.port.out.dto.RedeemResult
import com.goodpon.infra.redis.coupon.core.CouponTemplateStatsRedisCommandCache
import com.goodpon.infra.redis.coupon.core.CouponTemplateStatsRedisQueryCache
import org.springframework.stereotype.Component

@Component("partnerCouponTemplateStatsCacheAdapter")
class CouponTemplateStatsCacheAdapter(
    private val commandCache: CouponTemplateStatsRedisCommandCache,
    private val queryCache: CouponTemplateStatsRedisQueryCache,
) : CouponTemplateStatsCache {

    override fun issueCoupon(couponTemplateId: Long, userId: String, maxIssueCount: Long?): IssueResult {
        return commandCache.issueCoupon(
            couponTemplateId = couponTemplateId,
            userId = userId,
            maxIssueCount = maxIssueCount
        )
    }

    override fun redeemCoupon(couponTemplateId: Long, userId: String, maxRedeemCount: Long?): RedeemResult {
        return commandCache.redeemCoupon(
            couponTemplateId = couponTemplateId,
            userId = userId,
            maxRedeemCount = maxRedeemCount
        )
    }

    override fun cancelIssue(couponTemplateId: Long, userId: String) {
        commandCache.cancelIssue(couponTemplateId = couponTemplateId, userId = userId)
    }

    override fun cancelRedeem(couponTemplateId: Long, userId: String) {
        commandCache.cancelRedeem(couponTemplateId = couponTemplateId, userId = userId)
    }

    override fun getStats(couponTemplateId: Long): Pair<Long, Long> {
        return queryCache.getStats(couponTemplateId)
    }

    override fun getMultipleStats(couponTemplateIds: List<Long>): Map<Long, Pair<Long, Long>> {
        return queryCache.getMultipleStats(couponTemplateIds)
    }
}