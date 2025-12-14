package com.goodpon.domain.merchant

data class MerchantAccount private constructor(
    val id: Long = 0,
    val accountId: Long,
    val role: MerchantAccountRole,
) {

    companion object {
        fun createOwner(accountId: Long): MerchantAccount {
            return MerchantAccount(
                accountId = accountId,
                role = MerchantAccountRole.OWNER
            )
        }

        fun createStaff(accountId: Long): MerchantAccount {
            return MerchantAccount(
                accountId = accountId,
                role = MerchantAccountRole.STAFF
            )
        }

        fun reconstruct(id: Long, accountId: Long, role: MerchantAccountRole): MerchantAccount {
            return MerchantAccount(
                id = id,
                accountId = accountId,
                role = role
            )
        }
    }
}
