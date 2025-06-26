package com.goodpon.core.domain.merchant

import org.springframework.stereotype.Component

@Component
class MerchantAccountReader(
    private val merchantAccountRepository: MerchantAccountRepository,
) {

    fun readByMerchantIdAndAccountId(
        merchantId: Long,
        accountId: Long,
    ): MerchantAccount? {
        return merchantAccountRepository.findByMerchantIdAndAccountId(merchantId, accountId)
    }
}