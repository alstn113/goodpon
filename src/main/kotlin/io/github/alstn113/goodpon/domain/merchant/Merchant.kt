package io.github.alstn113.goodpon.domain.merchant

data class Merchant(
    val id: Long,
    val name: String,
    val businessNumber: String,
    val clientKey: String,
    val secretKey: String,
)
