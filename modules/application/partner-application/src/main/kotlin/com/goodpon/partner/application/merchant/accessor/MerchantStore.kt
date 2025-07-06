package com.goodpon.partner.application.merchant.accessor

import com.goodpon.domain.account.Account
import com.goodpon.domain.merchant.Merchant
import com.goodpon.domain.merchant.MerchantAccountRepository
import com.goodpon.domain.merchant.MerchantRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class MerchantStore(
    private val merchantRepository: MerchantRepository,
    private val merchantAccountRepository: MerchantAccountRepository,
) {

    @Transactional
    fun createMerchant(
        merchantName: String,
        account: Account,
    ): Pair<Merchant, com.goodpon.domain.merchant.MerchantAccount> {
        val merchant = Merchant.create(name = merchantName)
        val savedMerchant = merchantRepository.save(merchant)

        val merchantAccount = com.goodpon.domain.merchant.MerchantAccount.createOwner(
            merchantId = savedMerchant.id,
            accountId = account.id
        )
        val savedMerchantAccount = merchantAccountRepository.save(merchantAccount)

        return Pair(savedMerchant, savedMerchantAccount)
    }
}
