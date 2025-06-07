package io.github.alstn113.payments.domain.merchant

data class MerchantAccount(
    val id: Long = 0L,
    val mid: Long,
    val accountId: Long,
    val role: MerchantAccountRole,
)