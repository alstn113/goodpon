package com.goodpon.application.partner.coupon.port.out

interface CouponTemplateStatsCache {

    fun incrementIssueCount(couponTemplateId: Long, limit: Long?): Boolean

    fun cancelIssue(couponTemplateId: Long): Boolean

    fun incrementRedeemCount(couponTemplateId: Long, limit: Long?): Boolean

    fun cancelRedeem(couponTemplateId: Long): Boolean

    fun getStats(couponTemplateId: Long): Pair<Long, Long>

    fun getMultipleStats(couponTemplateIds: List<Long>): Map<Long, Pair<Long, Long>>
}