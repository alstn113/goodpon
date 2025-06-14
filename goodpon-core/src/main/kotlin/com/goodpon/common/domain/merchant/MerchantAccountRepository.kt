package com.goodpon.common.domain.merchant

interface MerchantAccountRepository {

    fun save(merchantAccount: MerchantAccount): MerchantAccount
}