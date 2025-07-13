package com.goodpon.dashboard.application.coupon.service.dto

import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import java.time.LocalDateTime

data class CouponTemplateSummary(
    val id: Long,
    val name: String,
    val description: String,
    val status: CouponTemplateStatus,
    val issuedCount: Long,
    val redeemedCount: Long,
    val createdAt: LocalDateTime,
)
