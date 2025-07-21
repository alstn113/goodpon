package com.goodpon.application.dashboard.merchant.port.`in`.dto

data class CreateMerchantCommand(
    val accountId: Long,
    val name: String,
)
