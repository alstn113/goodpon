package com.goodpon.application.couponissuer.port.out

interface CouponTemplateStatsCache {

    fun hasValidReservation(couponTemplateId: Long, userId: String): Boolean
    fun completeIssueCoupon(couponTemplateId: Long, userId: String)
    fun markAsFailedIssuance(couponTemplateId: Long, userId: String)
    fun cancelIssue(couponTemplateId: Long, userId: String)
}