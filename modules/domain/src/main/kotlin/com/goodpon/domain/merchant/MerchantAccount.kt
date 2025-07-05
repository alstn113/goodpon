package com.goodpon.domain.merchant

data class MerchantAccount private constructor(
    val id: Long = 0,
    val merchantId: Long,
    val accountId: Long,
    val role: com.goodpon.domain.merchant.MerchantAccountRole,
) {

    companion object {
        fun createOwner(merchantId: Long, accountId: Long): com.goodpon.domain.merchant.MerchantAccount {
            return com.goodpon.domain.merchant.MerchantAccount(
                merchantId = merchantId,
                accountId = accountId,
                role = com.goodpon.domain.merchant.MerchantAccountRole.OWNER
            )
        }

        fun reconstruct(
            id: Long,
            merchantId: Long,
            accountId: Long,
            role: com.goodpon.domain.merchant.MerchantAccountRole,
        ): com.goodpon.domain.merchant.MerchantAccount {
            return com.goodpon.domain.merchant.MerchantAccount(
                id = id,
                merchantId = merchantId,
                accountId = accountId,
                role = role
            )
        }
    }
}
