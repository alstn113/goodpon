package com.goodpon.domain.merchant

data class Merchant(
    val id: Long = 0,
    val name: String,
    val clientId: String,
    val secrets: List<MerchantClientSecret>,
    val accounts: List<MerchantAccount>,
) {

    companion object {
        fun reconstruct(
            id: Long,
            name: String,
            clientId: String,
            secrets: List<MerchantClientSecret>,
            accounts: List<MerchantAccount>,
        ): Merchant {
            return Merchant(
                id = id,
                name = name,
                clientId = clientId,
                secrets = secrets,
                accounts = accounts
            )
        }
    }
}