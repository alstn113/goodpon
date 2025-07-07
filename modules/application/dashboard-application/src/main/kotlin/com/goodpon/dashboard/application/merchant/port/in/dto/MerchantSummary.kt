package com.goodpon.dashboard.application.merchant.port.`in`.dto

import java.time.LocalDateTime

data class MerchantSummary(
    val id: Long,
    val name: String,
    val createdAt: LocalDateTime,
)
