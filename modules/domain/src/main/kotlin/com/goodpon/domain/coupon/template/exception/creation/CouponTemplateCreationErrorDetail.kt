package com.goodpon.domain.coupon.template.exception.creation

data class CouponTemplateCreationErrorDetail(
    val field: String,
    val rejectedValue: Any?,
    val message: String,
)
