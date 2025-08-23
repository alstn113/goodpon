package com.goodpon.application.partner.coupon.port.out

import com.goodpon.application.partner.coupon.port.out.dto.IssueResult
import com.goodpon.application.partner.coupon.port.out.dto.RedeemResult

interface CouponTemplateStatsCache {

    fun issueCoupon(couponTemplateId: Long, userId: String, maxIssueCount: Long?): IssueResult
    fun redeemCoupon(couponTemplateId: Long, userId: String, maxRedeemCount: Long?): RedeemResult
    fun cancelIssue(couponTemplateId: Long, userId: String)
    fun cancelRedeem(couponTemplateId: Long, userId: String)
    fun getStats(couponTemplateId: Long): Pair<Long, Long>
    fun getMultipleStats(couponTemplateIds: List<Long>): Map<Long, Pair<Long, Long>>
}