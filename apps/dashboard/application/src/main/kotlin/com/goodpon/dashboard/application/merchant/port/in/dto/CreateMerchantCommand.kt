package com.goodpon.dashboard.application.merchant.port.`in`.dto

data class CreateMerchantCommand(
    val accountId: Long,
    val name: String,
)
