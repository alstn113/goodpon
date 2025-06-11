package com.goodpon.api.dashboard.api.controller.v1.request

import com.goodpon.common.application.merchant.request.MerchantCreateRequest
import com.goodpon.common.domain.auth.AccountPrincipal

data class MerchantCreateWebRequest(
    val name: String,
    val businessNumber: String,
) {

    fun toAppRequest(accountPrincipal: AccountPrincipal): MerchantCreateRequest {
        return MerchantCreateRequest(
            accountPrincipal = accountPrincipal,
            name = name,
            businessNumber = businessNumber,
        )
    }
}
