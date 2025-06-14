package com.goodpon.core.domain.merchant

interface MerchantAccountRepository {

    fun save(merchantAccount: MerchantAccount): MerchantAccount
}