package com.goodpon.dashboard.application.merchant.port.out

import com.goodpon.domain.merchant.MerchantAccount

interface MerchantAccountRepository {

    fun save(merchantAccount: MerchantAccount): MerchantAccount

    fun findByMerchantIdAndAccountId(merchantId: Long, accountId: Long): MerchantAccount?
}