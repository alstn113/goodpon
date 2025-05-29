package io.github.alstn113.payments.application.merchant

import io.github.alstn113.payments.application.merchant.response.MerchantResponse
import io.github.alstn113.payments.domain.merchant.MerchantRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MerchantService(
    private val merchantRepository: MerchantRepository
) {

    @Transactional(readOnly = true)
    fun getMerchantByClientKey(clientKey: String): MerchantResponse {
        val merchant = merchantRepository.findByClientKey(clientKey)
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
        val merchant = merchantRepository.findBySecretKey(secretKey)
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