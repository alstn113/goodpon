package com.goodpon.domain.merchant

interface MerchantAccountRepository {

    fun save(merchantAccount: MerchantAccount): MerchantAccount

    fun findByMerchantIdAndAccountId(merchantId: Long, accountId: Long): MerchantAccount?
}