package com.goodpon.partner.application.coupon.service.listener

data class RedeemCouponRollbackEvent(
    val couponTemplateId: Long,
    val userId: String,
)
