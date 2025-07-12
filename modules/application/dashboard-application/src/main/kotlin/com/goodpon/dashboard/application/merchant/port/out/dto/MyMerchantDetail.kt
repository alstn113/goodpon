package com.goodpon.dashboard.application.merchant.port.out.dto

import com.goodpon.domain.merchant.MerchantAccountRole
import java.time.LocalDateTime

data class MyMerchantDetail(
    val id: Long,
    val name: String,
    val clientId: String,
    val accounts: List<MerchantAccountDetail>,
    val clientSecrets: List<MerchantClientSecretDetail>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {

    data class MerchantAccountDetail(
        val merchantAccountId: Long,
        val accountId: Long,
        val email: String,
        val name: String,
        val role: MerchantAccountRole,
        val createdAt: LocalDateTime,
    )

    data class MerchantClientSecretDetail(
        val merchantClientSecretId: Long,
        val secret: String,
        val createdAt: LocalDateTime,
        val expiredAt: LocalDateTime,
    )
}
