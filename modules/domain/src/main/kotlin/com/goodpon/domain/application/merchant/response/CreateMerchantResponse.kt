package com.goodpon.domain.application.merchant.response

import com.goodpon.domain.domain.merchant.MerchantAccountRole

data class CreateMerchantResponse(
    val id: Long,
    val name: String,
    val secretKey: String,
    val accounts: List<MerchantAccountInfo>,
)

data class MerchantAccountInfo(
    val id: Long,
    val accountId: Long,
    val role: MerchantAccountRole,
)
