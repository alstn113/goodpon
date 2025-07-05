package com.goodpon.api.dashboard.api.controller.v1.request

import com.goodpon.core.application.merchant.request.CreateMerchantRequest
import com.goodpon.core.application.auth.AccountPrincipal

data class CreateMerchantWebRequest(
    val name: String,
) {

    fun toAppRequest(accountPrincipal: AccountPrincipal): CreateMerchantRequest {
        return CreateMerchantRequest(
            accountPrincipal = accountPrincipal,
            name = name,
        )
    }
}
