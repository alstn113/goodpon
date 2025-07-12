package com.goodpon.dashboard.application.merchant.service.accessor

import com.goodpon.dashboard.application.merchant.port.out.MerchantRepository
import com.goodpon.domain.account.Account
import com.goodpon.domain.merchant.Merchant
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class MerchantStore(
    private val merchantRepository: MerchantRepository,
) {

    @Transactional
    fun createMerchant(name: String, account: Account): Merchant {
        val merchant = Merchant.create(name, account.id)
        val savedMerchant = merchantRepository.save(merchant)

        return savedMerchant
    }
}
