package com.goodpon.common.domain.merchant

import org.springframework.stereotype.Component

@Component
class MerchantReader(
    private val merchantRepository: MerchantRepository,
) {

    fun readByClientKey(clientKey: String): Merchant {
        return merchantRepository.findByClientKey(clientKey)
            ?: throw IllegalArgumentException("Merchant not found for client key: $clientKey")
    }

    fun readBySecretKey(secretKey: String): Merchant {
        return merchantRepository.findBySecretKey(secretKey)
            ?: throw IllegalArgumentException("Merchant not found for secret key: $secretKey")
    }
}