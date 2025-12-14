package com.goodpon.couponissuer.worker.dto

import com.goodpon.couponissuer.application.port.`in`.dto.IssueCouponCommand
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