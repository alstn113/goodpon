package com.goodpon.domain.application.merchant.request

data class CreateMerchantRequest(
    val accountId: Long,
    val name: String,
)
