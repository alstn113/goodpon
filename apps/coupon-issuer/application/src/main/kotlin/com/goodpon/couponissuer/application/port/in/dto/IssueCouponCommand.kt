package com.goodpon.couponissuer.application.port.`in`.dto

import java.time.LocalDateTime

data class IssueCouponCommand(
    val couponTemplateId: Long,
    val userId: String,
    val requestedAt: LocalDateTime,
)
