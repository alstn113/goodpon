package com.goodpon.core.domain.coupon

interface CouponTemplateStatsCounter {

    fun getStats(couponTemplateId: Long): CouponTemplateStats
    fun increaseIssueCount(couponTemplateId: Long, count: Long = 1L)
    fun increaseUseCount(couponTemplateId: Long, count: Long = 1L)
}