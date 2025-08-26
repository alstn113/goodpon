package com.goodpon.consumer.couponissuer.listener

import java.time.LocalDateTime

data class IssueCouponRequestedEvent(
    val couponTemplateId: Long,
    val userId: String,
    val requestedAt: LocalDateTime,
)
