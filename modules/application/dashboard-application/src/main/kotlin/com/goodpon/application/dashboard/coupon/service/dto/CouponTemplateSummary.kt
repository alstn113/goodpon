package com.goodpon.application.dashboard.coupon.service.dto

import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import java.time.LocalDateTime

data class CouponTemplateSummaries(
    val templates: List<CouponTemplateSummary>,
)

data class CouponTemplateSummary(
    val id: Long,
    val name: String,
    val description: String,
    val status: CouponTemplateStatus,
    val issueCount: Long,
    val redeemCount: Long,
    val createdAt: LocalDateTime,
)
