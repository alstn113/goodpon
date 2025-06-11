package com.goodpon.api.dashboard.api.controller.v1.request

import com.goodpon.common.application.merchant.request.CreateMerchantRequest
import com.goodpon.common.domain.auth.AccountPrincipal

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
