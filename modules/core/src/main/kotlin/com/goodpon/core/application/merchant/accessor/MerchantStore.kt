package com.goodpon.core.application.merchant.accessor

import com.goodpon.core.domain.UniqueIdGenerator
import com.goodpon.core.domain.account.Account
import com.goodpon.core.domain.merchant.Merchant
import com.goodpon.core.domain.merchant.MerchantAccount
import com.goodpon.core.domain.merchant.MerchantAccountRepository
import com.goodpon.core.domain.merchant.MerchantRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class MerchantStore(
    private val merchantRepository: MerchantRepository,
    private val merchantAccountRepository: MerchantAccountRepository,
    private val uniqueIdGenerator: UniqueIdGenerator,
) {
    @Transactional
    fun createMerchant(merchantName: String, account: Account): Pair<Merchant, MerchantAccount> {
        val merchant = Merchant(name = merchantName, secretKey = uniqueIdGenerator.generate())
        val savedMerchant = merchantRepository.save(merchant)

        val merchantAccount = MerchantAccount.createOwner(merchantId = savedMerchant.id, accountId = account.id)
        val savedMerchantAccount = merchantAccountRepository.save(merchantAccount)

        return Pair(savedMerchant, savedMerchantAccount)
    }
}
