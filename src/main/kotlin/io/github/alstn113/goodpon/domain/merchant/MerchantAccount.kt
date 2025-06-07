package io.github.alstn113.goodpon.domain.merchant

data class MerchantAccount(
    val id: Long? = null,
    val mid: Long,
    val accountId: Long,
    val role: MerchantAccountRole,
)