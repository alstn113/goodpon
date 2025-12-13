package com.goodpon.consumer.couponissuer.listener.dto

import com.goodpon.application.couponissuer.port.`in`.dto.IssueCouponCommand
import java.time.LocalDateTime

data class IssueCouponRequestedEvent(
    val couponTemplateId: Long,
    val userId: String,
    val requestedAt: LocalDateTime,
) {

    fun toIssueCouponCommand() = IssueCouponCommand(
        couponTemplateId = couponTemplateId,
        userId = userId,
        requestedAt = requestedAt,
    )
}