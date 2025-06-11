package com.goodpon.common.domain.coupon

import java.time.LocalDateTime

data class CouponTemplate(
    val id: Long,
    val merchantId: Long,
    val name: String,
    val description: String,
    val discountType: DiscountType,
    val discountValue: Int,
    val minimumOrderAmount: Int,
    val status: CouponTemplateStatus, // 쿠폰의 주요한 생명 주기
    val isIssuable: Boolean, // 보조 제어 플래그
    val isUsable: Boolean, // 보조 제어 플래스
    val issueStartAt: LocalDateTime,
    val issueEndAt: LocalDateTime?,
    val validityDurationSeconds: Long?,
    val useEndAt: LocalDateTime?,
    val limitType: CouponTemplateLimitType,
    val issueCount: Long,
    val issueLimit: Long,
    val useCount: Long,
    val useLimit: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
