package com.goodpon.dashboard.api.controller.v1.merchant.dto

import com.goodpon.dashboard.application.merchant.port.`in`.dto.CreateMerchantCommand

data class CreateMerchantRequest(
    val name: String,
) {

    fun toCommand(accountId: Long): CreateMerchantCommand {
        return CreateMerchantCommand(
            name = name,
            accountId = accountId,
        )
    }
}