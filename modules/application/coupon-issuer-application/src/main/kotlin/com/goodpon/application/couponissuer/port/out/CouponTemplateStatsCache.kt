package com.goodpon.application.couponissuer.port.out

interface CouponTemplateStatsCache {

    fun completeIssueCoupon(couponTemplateId: Long, userId: String)
    fun cancelIssue(couponTemplateId: Long, userId: String)
}