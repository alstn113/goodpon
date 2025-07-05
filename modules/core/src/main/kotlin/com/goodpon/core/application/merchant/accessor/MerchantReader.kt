package com.goodpon.core.application.merchant.accessor

import com.goodpon.core.domain.merchant.exception.MerchantNotFoundException
import com.goodpon.core.domain.merchant.Merchant
import com.goodpon.core.domain.merchant.MerchantRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class MerchantReader(
    private val merchantRepository: MerchantRepository,
) {

    @Transactional(readOnly = true)
    fun readById(merchantId: Long): Merchant {
        return merchantRepository.findById(merchantId)
            ?: throw MerchantNotFoundException()
    }

    @Transactional(readOnly = true)
    fun readBySecretKey(secretKey: String): Merchant {
        return merchantRepository.findBySecretKey(secretKey)
            ?: throw MerchantNotFoundException()
    }
}