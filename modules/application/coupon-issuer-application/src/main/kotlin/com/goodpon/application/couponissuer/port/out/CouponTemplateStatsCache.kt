package com.goodpon.application.couponissuer.port.out

interface CouponTemplateStatsCache {

    fun cancelIssue(couponTemplateId: Long, userId: String)
}