package com.goodpon.application.dashboard.merchant.service.accessor

import com.goodpon.application.dashboard.merchant.port.out.MerchantRepository
import com.goodpon.application.dashboard.merchant.port.out.exception.MerchantNotFoundException
import com.goodpon.domain.account.Account
import com.goodpon.domain.merchant.Merchant
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class MerchantAccessor(
    private val merchantRepository: MerchantRepository,
) {

    @Transactional(readOnly = true)
    @Cacheable(value = ["merchants:byId"], key = "#merchantId")
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