package com.goodpon.application.dashboard.coupon.service.dto

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
