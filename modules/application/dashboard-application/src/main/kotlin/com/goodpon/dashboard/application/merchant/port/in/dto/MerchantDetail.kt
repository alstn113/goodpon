package com.goodpon.dashboard.application.merchant.port.`in`.dto

import com.goodpon.domain.merchant.MerchantAccountRole
import java.time.LocalDateTime

data class MerchantDetail(
    val merchantId: Long,
    val name: String,
    val secretKey: String,
    val accounts: List<MerchantAccountInfo>,
    val createdAt: LocalDateTime,
) {

    data class MerchantAccountInfo(
        val merchantAccountId: Long,
        val accountId: Long,
        val name: String,
        val email: String,
        val role: MerchantAccountRole,
        val createdAt: LocalDateTime,
    )
}
