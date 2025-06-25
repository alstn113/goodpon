package com.goodpon.core.application.merchant.response

import com.goodpon.core.domain.merchant.MerchantAccountRole

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
