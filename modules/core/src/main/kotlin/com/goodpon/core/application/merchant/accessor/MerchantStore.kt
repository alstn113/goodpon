package com.goodpon.core.application.merchant.accessor

import com.goodpon.core.domain.account.Account
import com.goodpon.core.domain.merchant.*
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class MerchantStore(
    private val merchantRepository: MerchantRepository,
    private val merchantAccountRepository: MerchantAccountRepository,
    private val secretKeyGenerator: SecretKeyGenerator,
) {

    @Transactional
    fun createMerchant(merchantName: String, account: Account): Pair<Merchant, MerchantAccount> {
        val merchant = Merchant(name = merchantName, secretKey = secretKeyGenerator.generate())
        val savedMerchant = merchantRepository.save(merchant)

        val merchantAccount = MerchantAccount.createOwner(merchantId = savedMerchant.id, accountId = account.id)
        val savedMerchantAccount = merchantAccountRepository.save(merchantAccount)

        return Pair(savedMerchant, savedMerchantAccount)
    }
}
