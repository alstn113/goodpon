package com.goodpon.api.dashboard.controller.v1.merchant.dto

import com.goodpon.application.dashboard.merchant.port.`in`.dto.CreateMerchantCommand

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