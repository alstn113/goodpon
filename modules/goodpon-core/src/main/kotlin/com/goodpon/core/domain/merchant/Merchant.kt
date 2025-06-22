package com.goodpon.core.domain.merchant

data class Merchant(
    val id: Long = 0,
    val name: String,
    val secretKey: String,
)