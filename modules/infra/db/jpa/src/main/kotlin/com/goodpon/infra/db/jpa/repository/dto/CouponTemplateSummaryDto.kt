package com.goodpon.infra.db.jpa.repository.dto

import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import java.time.LocalDateTime

data class CouponTemplateSummaryDto(
    val id: Long,
    val name: String,
    val description: String,
    val status: CouponTemplateStatus,
    val issuedCount: Long,
    val redeemedCount: Long,
    val createdAt: LocalDateTime,
)