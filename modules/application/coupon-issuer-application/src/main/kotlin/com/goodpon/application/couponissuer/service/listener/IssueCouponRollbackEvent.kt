package com.goodpon.application.couponissuer.service.listener

data class IssueCouponRollbackEvent(
    val couponTemplateId: Long,
    val userId: String,
)
