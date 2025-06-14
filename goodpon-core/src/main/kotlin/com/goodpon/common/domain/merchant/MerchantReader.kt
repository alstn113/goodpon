package com.goodpon.common.domain.merchant

import org.springframework.stereotype.Component

@Component
class MerchantReader(
    private val merchantRepository: MerchantRepository,
) {

    fun readBySecretKey(secretKey: String): Merchant {
        return merchantRepository.findBySecretKey(secretKey)
            ?: throw IllegalArgumentException("Merchant not found for secret key: $secretKey")
    }
}