package com.goodpon.domain.application.merchant.accessor

import com.goodpon.domain.merchant.MerchantAccount
import com.goodpon.domain.domain.merchant.MerchantAccountRepository
import com.goodpon.domain.domain.merchant.exception.MerchantNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class MerchantAccountReader(
    private val merchantAccountRepository: MerchantAccountRepository,
) {

    @Transactional(readOnly = true)
    fun readByMerchantIdAndAccountId(merchantId: Long, accountId: Long): com.goodpon.domain.merchant.MerchantAccount {
        return merchantAccountRepository.findByMerchantIdAndAccountId(merchantId, accountId)
            ?: throw MerchantNotFoundException()
    }
}