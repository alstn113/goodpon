package com.goodpon.infra.jpa.repository.dto

import java.time.LocalDateTime

data class MyMerchantSummaryDto(
    val id: Long,
    val name: String,
    val createdAt: LocalDateTime,
)