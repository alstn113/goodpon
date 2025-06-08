package io.github.alstn113.goodpon.domain.merchant

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class MerchantReader(
    private val merchantRepository: MerchantRepository,
) {

    @Transactional(readOnly = true)
    fun readByClientKey(clientKey: String): Merchant {
        return merchantRepository.findByClientKey(clientKey)
            ?: throw IllegalArgumentException("Merchant not found for client key: $clientKey")
    }

    @Transactional(readOnly = true)
    fun readBySecretKey(secretKey: String): Merchant {
        return merchantRepository.findBySecretKey(secretKey)
            ?: throw IllegalArgumentException("Merchant not found for secret key: $secretKey")
    }
}