package com.goodpon.dashboard.application.merchant.port.`in`.dto

import com.goodpon.domain.merchant.MerchantAccountRole

data class CreateMerchantResult(
    val id: Long,
    val name: String,
    val secretKey: String,
    val accounts: List<MerchantOwnerInfo>,
) {

    data class MerchantOwnerInfo(
        val id: Long,
        val accountId: Long,
        val role: MerchantAccountRole,
    )
}

//
//data class CreateMerchantResult(
//    val merchantId: Long,
//    val name: String,
//    val secretKey: String,
//    val owner: MerchantOwnerInfo,
//    val createdAt: LocalDateTime,
//) {
//
//    data class MerchantOwnerInfo(
//        val merchantAccountId: Long,
//        val accountId: Long,
//        val name: String,
//        val email: String,
//        val role: MerchantAccountRole,
//        val createdAt: LocalDateTime,
//    )
//}
