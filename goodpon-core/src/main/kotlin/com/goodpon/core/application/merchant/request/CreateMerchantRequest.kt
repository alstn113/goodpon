package com.goodpon.core.application.merchant.request

import com.goodpon.core.domain.auth.AccountPrincipal

data class CreateMerchantRequest(
    val accountPrincipal: AccountPrincipal,
    val name: String,
)
