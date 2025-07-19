package com.goodpon.infra.db.jpa.repository.dto

import com.goodpon.domain.coupon.history.CouponActionType
import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

@QueryProjection
data class CouponHistorySummaryDto(
    val historyId: String,
    val actionType: CouponActionType,
    val recordedAt: LocalDateTime,
    val orderId: String?,
    val reason: String?,
    val userCouponId: String,
    val userId: String,
    val couponTemplateId: Long,
    val couponTemplateName: String,
)