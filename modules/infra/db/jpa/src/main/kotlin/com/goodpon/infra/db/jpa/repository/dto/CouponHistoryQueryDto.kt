package com.goodpon.infra.db.jpa.repository.dto

import java.time.LocalDate

data class CouponHistoryQueryDto(
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val userId: String?,
    val orderId: String?,
    val couponTemplateId: Long?,
    val nextCursor: String?,
    val size: Int,
)