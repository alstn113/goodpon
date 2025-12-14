package com.goodpon.partner.application.coupon.port.out

import com.goodpon.partner.application.coupon.port.out.dto.IssueResult
import com.goodpon.partner.application.coupon.port.out.dto.RedeemResult

interface CouponIssueStore {

    fun reserveCoupon(couponTemplateId: Long, userId: String, maxIssueCount: Long?): IssueResult
    fun redeemCoupon(couponTemplateId: Long, userId: String, maxRedeemCount: Long?): RedeemResult
    fun cancelIssue(couponTemplateId: Long, userId: String)
    fun cancelRedeem(couponTemplateId: Long, userId: String)
}