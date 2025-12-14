package com.goodpon.dashboard.application.coupon.service.dto

import java.time.LocalDate

data class GetCouponHistoriesQuery(
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val userId: String?,
    val orderId: String?,
    val couponTemplateId: Long?,
    val nextCursor: String?,
    val size: Int,
)
