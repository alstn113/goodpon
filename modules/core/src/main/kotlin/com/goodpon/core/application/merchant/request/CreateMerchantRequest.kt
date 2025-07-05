package com.goodpon.core.application.merchant.request

data class CreateMerchantRequest(
    val accountId: Long,
    val name: String,
)
