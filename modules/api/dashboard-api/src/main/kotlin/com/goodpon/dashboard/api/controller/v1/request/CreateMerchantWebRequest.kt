package com.goodpon.dashboard.api.controller.v1.request

import com.goodpon.domain.application.merchant.request.CreateMerchantRequest

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
