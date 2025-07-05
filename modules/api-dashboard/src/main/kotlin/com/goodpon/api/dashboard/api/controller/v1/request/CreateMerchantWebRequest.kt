package com.goodpon.api.dashboard.api.controller.v1.request

import com.goodpon.core.application.merchant.request.CreateMerchantRequest

data class CreateMerchantWebRequest(
    val name: String,
) {

    fun toAppRequest(accountId: Long): CreateMerchantRequest {
        return CreateMerchantRequest(
            accountId = accountId,
            name = name,
        )
    }
}
