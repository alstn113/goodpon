package com.goodpon.common.application.merchant.response

data class MerchantResponse(
    val id: Long,
    val name: String,
    val businessNumber: String,
    val clientKey: String,
    val secretKey: String,
)
