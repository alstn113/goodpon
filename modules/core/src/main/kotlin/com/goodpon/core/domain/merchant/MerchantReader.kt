package com.goodpon.core.domain.merchant

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class MerchantReader(
    private val merchantRepository: MerchantRepository,
) {

    @Transactional(readOnly = true)
    fun readById(merchantId: Long): Merchant {
        return merchantRepository.findById(merchantId)
            ?: throw IllegalArgumentException("Merchant not found for ID: $merchantId")
    }

    @Transactional(readOnly = true)
    fun readBySecretKey(secretKey: String): Merchant {
        return merchantRepository.findBySecretKey(secretKey)
            ?: throw IllegalArgumentException("Merchant not found for secret key: $secretKey")
    }
}