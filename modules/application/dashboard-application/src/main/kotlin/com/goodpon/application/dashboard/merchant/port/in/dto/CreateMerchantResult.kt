package com.goodpon.application.dashboard.merchant.port.`in`.dto

import com.goodpon.domain.merchant.Merchant
import com.goodpon.domain.merchant.MerchantAccountRole
import java.time.LocalDateTime

data class CreateMerchantResult(
    val id: Long,
    val name: String,
    val clientId: String,
    val clientSecrets: List<ClientSecretInfo>,
    val merchantAccounts: List<MerchantAccountInfo>,
) {

    data class MerchantAccountInfo(
        val id: Long,
        val accountId: Long,
        val role: MerchantAccountRole,
    )

    data class ClientSecretInfo(
        val id: Long,
        val secret: String,
        val expiredAt: LocalDateTime?,
    )

    companion object {
        fun from(merchant: Merchant): CreateMerchantResult {
            val merchantAccounts = merchant.accounts.map { account ->
                MerchantAccountInfo(
                    id = account.id,
                    accountId = account.accountId,
                    role = account.role
                )
            }
            val clientSecrets = merchant.secrets.map { secret ->
                ClientSecretInfo(
                    id = secret.id,
                    secret = secret.secret,
                    expiredAt = secret.expiredAt
                )
            }

            return CreateMerchantResult(
                id = merchant.id,
                name = merchant.name,
                clientId = merchant.clientId,
                clientSecrets = clientSecrets,
                merchantAccounts = merchantAccounts
            )
        }
    }
}
