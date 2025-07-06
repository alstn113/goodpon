package com.goodpon.dashboard.application.merchant.request

data class CreateMerchantRequest(
    val accountId: Long,
    val name: String,
)
