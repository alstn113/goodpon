package com.goodpon.partner.infra.store

import com.goodpon.infra.redis.coupon.CouponIssueRedisStore
import com.goodpon.infra.redis.coupon.CouponIssueResult
import com.goodpon.infra.redis.coupon.CouponRedeemResult
import com.goodpon.partner.application.coupon.port.out.CouponIssueStore
import com.goodpon.partner.application.coupon.port.out.dto.IssueResult
import com.goodpon.partner.application.coupon.port.out.dto.RedeemResult
import org.springframework.stereotype.Component

@Component
class CouponIssueRedisStoreAdapter(
    private val couponIssueStore: CouponIssueRedisStore,
) : CouponIssueStore {

    override fun reserveCoupon(
        couponTemplateId: Long,
        userId: String,
        maxIssueCount: Long?,
    ): IssueResult {
        val result = couponIssueStore.reserve(
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
        val result = couponIssueStore.redeem(
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
        couponIssueStore.cancelIssue(couponTemplateId = couponTemplateId, userId = userId)
    }

    override fun cancelRedeem(couponTemplateId: Long, userId: String) {
        couponIssueStore.cancelRedeem(couponTemplateId = couponTemplateId, userId = userId)
    }
}