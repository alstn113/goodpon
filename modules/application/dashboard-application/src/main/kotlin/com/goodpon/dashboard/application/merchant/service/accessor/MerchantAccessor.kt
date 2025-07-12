package com.goodpon.dashboard.application.merchant.service.accessor

import com.goodpon.dashboard.application.merchant.port.out.MerchantRepository
import com.goodpon.dashboard.application.merchant.port.out.exception.MerchantNotFoundException
import com.goodpon.domain.account.Account
import com.goodpon.domain.merchant.Merchant
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class MerchantAccessor(
    private val merchantRepository: MerchantRepository,
) {

    @Transactional(readOnly = true)
    fun readById(merchantId: Long): Merchant {
        return merchantRepository.findById(merchantId)
            ?: throw MerchantNotFoundException()
    }

    @Transactional
    fun createMerchant(name: String, account: Account): Merchant {
        val merchant = Merchant.create(name, account.id)
        val savedMerchant = merchantRepository.save(merchant)

        return savedMerchant
    }
}