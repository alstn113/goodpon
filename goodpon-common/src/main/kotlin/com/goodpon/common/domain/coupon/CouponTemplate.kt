package com.goodpon.common.domain.coupon

import java.time.LocalDateTime

data class CouponTemplate(
    val id: Long,
    val merchantId: Long,
    val name: String,
    val description: String,
    val discountPolicy: DiscountPolicy,
    val couponPeriod: CouponPeriod,
    val usageLimit: UsageLimit,
    val status: CouponTemplateStatus, // 쿠폰의 주요한 생명 주기
    val isIssuable: Boolean, // 보조 제어 플래그
    val isUsable: Boolean, // 보조 제어 플래스
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
