package com.goodpon.core.application.merchant

import com.goodpon.core.domain.merchant.MerchantAccount
import com.goodpon.core.domain.merchant.MerchantAccountRepository
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