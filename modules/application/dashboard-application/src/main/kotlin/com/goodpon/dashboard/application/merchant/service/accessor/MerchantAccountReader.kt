package com.goodpon.dashboard.application.merchant.service.accessor

import com.goodpon.dashboard.application.merchant.port.out.MerchantAccountRepository
import com.goodpon.dashboard.application.merchant.port.out.exception.MerchantNotFoundException
import com.goodpon.domain.merchant.MerchantAccount
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