package com.goodpon.domain.merchant

interface MerchantAccountRepository {

    fun save(merchantAccount: com.goodpon.domain.merchant.MerchantAccount): com.goodpon.domain.merchant.MerchantAccount

    fun findByMerchantIdAndAccountId(merchantId: Long, accountId: Long): com.goodpon.domain.merchant.MerchantAccount?
}