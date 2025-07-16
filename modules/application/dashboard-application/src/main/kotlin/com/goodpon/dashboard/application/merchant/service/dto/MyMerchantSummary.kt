package com.goodpon.dashboard.application.merchant.service.dto

import java.time.LocalDateTime

data class MyMerchantSummaries(
    val merchants: List<MyMerchantSummary>,
)

data class MyMerchantSummary(
    val id: Long,
    val name: String,
    val createdAt: LocalDateTime,
)
