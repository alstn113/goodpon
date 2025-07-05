package com.goodpon.core.application.merchant.accessor

import com.goodpon.core.domain.merchant.exception.MerchantNotFoundException
import com.goodpon.core.domain.merchant.MerchantAccount
import com.goodpon.core.domain.merchant.MerchantAccountRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class MerchantAccountReader(
    private val merchantAccountRepository: MerchantAccountRepository,
) {

    @Transactional(readOnly = true)
    fun readByMerchantIdAndAccountId(merchantId: Long, accountId: Long): MerchantAccount {
        return merchantAccountRepository.findByMerchantIdAndAccountId(merchantId, accountId)
            ?: throw MerchantNotFoundException()
    }
}