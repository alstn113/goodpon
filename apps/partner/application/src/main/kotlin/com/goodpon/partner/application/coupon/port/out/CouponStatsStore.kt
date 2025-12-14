package com.goodpon.partner.application.coupon.port.out

interface CouponStatsStore {

    fun getStats(couponTemplateId: Long): Pair<Long, Long>
    fun getMultipleStats(couponTemplateIds: List<Long>): Map<Long, Pair<Long, Long>>
}