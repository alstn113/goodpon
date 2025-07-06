package com.goodpon.dashboard.application.merchant.port.`in`.dto

import com.goodpon.domain.merchant.MerchantAccountRole

data class CreateMerchantResult(
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
