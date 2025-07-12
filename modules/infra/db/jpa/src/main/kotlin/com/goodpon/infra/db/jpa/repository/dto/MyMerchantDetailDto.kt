package com.goodpon.infra.db.jpa.repository.dto

import com.goodpon.domain.merchant.MerchantAccountRole
import java.time.LocalDateTime

data class MyMerchantDetailDto(
    val id: Long,
    val name: String,
    val clientId: String,
    val merchantAccounts: List<MerchantAccountDetailDto>,
    val clientSecrets: List<MerchantClientSecretDetailDto>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)

data class MyMerchantDetailSummaryDto(
    val id: Long,
    val name: String,
    val clientId: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)

data class MerchantAccountDetailDto(
    val merchantAccountId: Long,
    val accountId: Long,
    val email: String,
    val name: String,
    val role: MerchantAccountRole,
    val createdAt: LocalDateTime,
)

data class MerchantClientSecretDetailDto(
    val merchantClientSecretId: Long,
    val secret: String,
    val createdAt: LocalDateTime,
    val expiredAt: LocalDateTime?,
)
