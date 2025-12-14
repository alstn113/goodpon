package com.goodpon.domain.merchant

import java.time.LocalDateTime
import java.util.*

data class Merchant(
    val id: Long = 0,
    val name: String,
    val clientId: String,
    val secrets: List<MerchantClientSecret>,
    val accounts: List<MerchantAccount>,
) {

    fun issueNewSecret(): Merchant {
        val newSecret = MerchantClientSecret.create(secret = generateClientSecret())
        return this.copy(secrets = secrets + newSecret)
    }

    fun expireSecret(secretId: Long, expiredAt: LocalDateTime): Merchant {
        val updatedSecrets = secrets.map { secret ->
            if (secret.id == secretId) {
                secret.expire(expiredAt)
            } else {
                secret
            }
        }
        return this.copy(secrets = updatedSecrets)
    }

    fun isValidClientSecret(clientSecret: String): Boolean {
        return secrets.any { it.secret == clientSecret && !it.isExpired() }
    }

    fun isAccessibleBy(accountId: Long): Boolean {
        return accounts.any { it.accountId == accountId }
    }

    companion object {
        fun create(name: String, accountId: Long): Merchant {
            val clientId = generateClientId()
            val clientSecret = generateClientSecret()

            val merchantAccount = MerchantAccount.createOwner(accountId = accountId)
            val secret = MerchantClientSecret.create(secret = clientSecret)

            return Merchant(
                name = name,
                clientId = clientId,
                secrets = listOf(secret),
                accounts = listOf(merchantAccount)
            )
        }

        private fun generateClientId(): String {
            return "ck_" + UUID.randomUUID().toString().replace("-", "")
        }

        private fun generateClientSecret(): String {
            return "sk_" + UUID.randomUUID().toString().replace("-", "")
        }

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