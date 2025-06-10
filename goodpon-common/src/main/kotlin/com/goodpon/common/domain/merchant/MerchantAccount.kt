package com.goodpon.common.domain.merchant

data class MerchantAccount(
    val id: Long,
    val mid: Long,
    val accountId: Long,
    val role: MerchantAccountRole,
)
