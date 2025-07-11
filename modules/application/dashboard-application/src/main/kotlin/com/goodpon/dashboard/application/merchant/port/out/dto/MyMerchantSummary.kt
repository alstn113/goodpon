package com.goodpon.dashboard.application.merchant.port.out.dto

import java.time.LocalDateTime

data class MyMerchantSummary(
    val id: Long,
    val name: String,
    val createdAt: LocalDateTime,
)
