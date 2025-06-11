package com.goodpon.common.domain.merchant

data class MerchantAccount(
    val id: Long = 0,
    val merchantId: Long,
    val accountId: Long,
    val role: MerchantAccountRole,
) {

    companion object {

        fun createOwner(merchantId: Long, accountId: Long): MerchantAccount {
            return MerchantAccount(
                merchantId = merchantId,
                accountId = accountId,
                role = MerchantAccountRole.OWNER
            )
        }
    }
}
