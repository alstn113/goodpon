package com.goodpon.api.dashboard.controller.v1.coupon.dto

import com.goodpon.api.dashboard.controller.v1.coupon.validation.CouponHistorySearchValid
import com.goodpon.application.dashboard.coupon.service.dto.GetCouponHistoriesQuery
import java.time.LocalDate

@CouponHistorySearchValid
data class CouponHistorySearchRequest(
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val userId: String? = null,
    val orderId: String? = null,
    val couponTemplateId: Long? = null,
    val nextCursor: String? = null,
    val size: Int = 20,
) {

    fun toQuery(): GetCouponHistoriesQuery {
        return GetCouponHistoriesQuery(
            startDate = startDate,
            endDate = endDate,
            userId = userId,
            orderId = orderId,
            couponTemplateId = couponTemplateId,
            nextCursor = nextCursor,
            size = size
        )
    }
}