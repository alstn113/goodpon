package com.goodpon.common.application.merchant.response

data class MerchantInfo(
    val id: Long,
    val name: String,
    val businessNumber: String,
    val secretKey: String,
)
