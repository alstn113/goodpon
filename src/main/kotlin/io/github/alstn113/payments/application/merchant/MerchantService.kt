package io.github.alstn113.payments.application.merchant

import io.github.alstn113.payments.application.merchant.response.MerchantResponse
import io.github.alstn113.payments.infra.persistence.merchant.MerchantJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MerchantService(
    private val merchantJpaRepository: MerchantJpaRepository
) {

    @Transactional(readOnly = true)
    fun getMerchantByClientKey(clientKey: String): MerchantResponse {
        val merchant = merchantJpaRepository.findByClientKey(clientKey)
            ?: throw IllegalArgumentException("Merchant not found for client key")

        return MerchantResponse(
            id = merchant.id,
            name = merchant.name,
            businessNumber = merchant.businessNumber,
            clientKey = merchant.clientKey,
            secretKey = merchant.secretKey
        )
    }

    @Transactional(readOnly = true)
    fun getMerchantBySecretKey(secretKey: String): MerchantResponse {
        val merchant = merchantJpaRepository.findBySecretKey(secretKey)
            ?: throw IllegalArgumentException("Merchant not found for secret key")

        return MerchantResponse(
            id = merchant.id,
            name = merchant.name,
            businessNumber = merchant.businessNumber,
            clientKey = merchant.clientKey,
            secretKey = merchant.secretKey
        )
    }
}