package com.goodpon.domain.merchant

import java.util.*

object MerchantFactory {

    private const val CLIENT_ID_PREFIX = "ck_"
    private const val CLIENT_SECRET_PREFIX = "sk_"

    fun create(name: String, accountId: Long): Merchant {
        val clientId = generateClientId()
        val clientSecret = generateClientSecret()

        val merchantAccount = MerchantAccount.createOwner(accountId = accountId)
        val secret = MerchantClientSecret.create(key = clientSecret)

        return Merchant(
            name = name,
            clientId = clientId,
            secrets = listOf(secret),
            accounts = listOf(merchantAccount)
        )
    }

    private fun generateClientId(): String {
        return CLIENT_ID_PREFIX + UUID.randomUUID().toString().replace("-", "")
    }

    private fun generateClientSecret(): String {
        return CLIENT_SECRET_PREFIX + UUID.randomUUID().toString().replace("-", "")
    }
}