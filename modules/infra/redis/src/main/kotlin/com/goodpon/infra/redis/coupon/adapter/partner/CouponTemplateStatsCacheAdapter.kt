package com.goodpon.infra.redis.coupon.adapter.partner

import com.goodpon.application.partner.coupon.port.out.CouponTemplateStatsCache
import com.goodpon.application.partner.coupon.port.out.dto.IssueResult
import com.goodpon.application.partner.coupon.port.out.dto.RedeemResult
import com.goodpon.infra.redis.coupon.core.CouponIssueResult
import com.goodpon.infra.redis.coupon.core.CouponRedeemResult
import com.goodpon.infra.redis.coupon.core.CouponTemplateStatsRedisCommandCache
import com.goodpon.infra.redis.coupon.core.CouponTemplateStatsRedisQueryCache
import org.springframework.stereotype.Component

@Component("partnerCouponTemplateStatsCacheAdapter")
class CouponTemplateStatsCacheAdapter(
    private val commandCache: CouponTemplateStatsRedisCommandCache,
    private val queryCache: CouponTemplateStatsRedisQueryCache,
) : CouponTemplateStatsCache {

    override fun reserveCoupon(
        couponTemplateId: Long,
        userId: String,
        maxIssueCount: Long?,
    ): IssueResult {
        val result = commandCache.reserveCoupon(
            couponTemplateId = couponTemplateId,
            userId = userId,
            maxIssueCount = maxIssueCount
        )

        return when (result) {
            CouponIssueResult.SUCCESS -> IssueResult.SUCCESS
            CouponIssueResult.ALREADY_ISSUED -> IssueResult.ALREADY_ISSUED
            CouponIssueResult.ISSUE_LIMIT_EXCEEDED -> IssueResult.ISSUE_LIMIT_EXCEEDED
        }
    }

    override fun redeemCoupon(couponTemplateId: Long, userId: String, maxRedeemCount: Long?): RedeemResult {
        val result = commandCache.redeemCoupon(
            couponTemplateId = couponTemplateId,
            userId = userId,
            maxRedeemCount = maxRedeemCount
        )

        return when (result) {
            CouponRedeemResult.SUCCESS -> RedeemResult.SUCCESS
            CouponRedeemResult.ALREADY_REDEEMED -> RedeemResult.ALREADY_REDEEMED
            CouponRedeemResult.REDEEM_LIMIT_EXCEEDED -> RedeemResult.REDEEM_LIMIT_EXCEEDED
        }
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