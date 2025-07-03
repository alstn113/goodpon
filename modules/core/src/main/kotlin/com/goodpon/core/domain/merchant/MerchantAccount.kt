package com.goodpon.core.domain.merchant

data class MerchantAccount private constructor(
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

        fun reconstruct(
            id: Long,
            merchantId: Long,
            accountId: Long,
            role: MerchantAccountRole,
        ): MerchantAccount {
            return MerchantAccount(
                id = id,
                merchantId = merchantId,
                accountId = accountId,
                role = role
            )
        }
    }
}
