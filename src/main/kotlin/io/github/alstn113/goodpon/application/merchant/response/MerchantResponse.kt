package io.github.alstn113.goodpon.application.merchant.response

data class MerchantResponse(
    val id: Long,
    val name: String,
    val businessNumber: String,
    val clientKey: String,
    val secretKey: String,
)