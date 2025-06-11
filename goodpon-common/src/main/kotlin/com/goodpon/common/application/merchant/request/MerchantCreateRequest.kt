package com.goodpon.common.application.merchant.request

import com.goodpon.common.domain.auth.AccountPrincipal

data class MerchantCreateRequest(
    val accountPrincipal: AccountPrincipal,
    val name: String,
    val businessNumber: String,
)
