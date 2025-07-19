package com.goodpon.dashboard.application.coupon.service.dto

import com.goodpon.domain.coupon.history.CouponActionType
import java.time.LocalDateTime

data class CouponHistoryQueryResult(
    val content: List<CouponHistorySummary>,
    val size: Int, // 현재 페이지 실제 데이터 개수
    val hasMore: Boolean,
    val nextCursor: String?, // coupon history id
)

data class CouponHistorySummary(
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
