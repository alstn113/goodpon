package com.goodpon.dashboard.application.merchant.service.accessor

import com.goodpon.dashboard.application.merchant.port.out.MerchantAccountRepository
import com.goodpon.dashboard.application.merchant.port.out.MerchantRepository
import com.goodpon.domain.account.Account
import com.goodpon.domain.merchant.Merchant
import com.goodpon.domain.merchant.MerchantAccount
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
    ): Pair<Merchant, MerchantAccount> {
        val merchant = Merchant.create(name = merchantName)
        val savedMerchant = merchantRepository.save(merchant)

        val merchantAccount = MerchantAccount.createOwner(
            merchantId = savedMerchant.id,
            accountId = account.id
        )
        val savedMerchantAccount = merchantAccountRepository.save(merchantAccount)

        return Pair(savedMerchant, savedMerchantAccount)
    }
}
