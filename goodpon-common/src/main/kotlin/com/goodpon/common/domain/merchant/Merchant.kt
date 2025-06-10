package com.goodpon.common.domain.merchant

data class Merchant(
    val id: Long,
    val name: String,
    val businessNumber: String,
    val clientKey: String,
    val secretKey: String,
)
