package com.goodpon.couponissuer.application.port.out

interface CouponIssueStore {

    fun existsIssueReservation(couponTemplateId: Long, userId: String): Boolean
    fun completeIssueCoupon(couponTemplateId: Long, userId: String)
    fun markIssueReservationAsFailed(couponTemplateId: Long, userId: String)
    fun cancelIssue(couponTemplateId: Long, userId: String)
}