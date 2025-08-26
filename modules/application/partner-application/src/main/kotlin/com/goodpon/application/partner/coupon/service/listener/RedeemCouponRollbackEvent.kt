package com.goodpon.application.partner.coupon.service.listener

data class RedeemCouponRollbackEvent(
    val couponTemplateId: Long,
    val userId: String,
)
