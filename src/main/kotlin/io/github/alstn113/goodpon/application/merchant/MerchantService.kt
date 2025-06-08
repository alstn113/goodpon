package io.github.alstn113.goodpon.application.merchant

import io.github.alstn113.goodpon.application.merchant.response.MerchantResponse
import io.github.alstn113.goodpon.domain.merchant.MerchantReader
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MerchantService(
    private val merchantReader: MerchantReader,
) {

    @Transactional(readOnly = true)
    fun getMerchantByClientKey(clientKey: String): MerchantResponse {
        val merchant = merchantReader.readByClientKey(clientKey)

        return MerchantResponse(
            id = merchant.id,
            name = merchant.name,
            businessNumber = merchant.businessNumber,
            clientKey = merchant.clientKey,
            secretKey = merchant.secretKey,
        )
    }

    @Transactional(readOnly = true)
    fun getMerchantBySecretKey(secretKey: String): MerchantResponse {
        val merchant = merchantReader.readBySecretKey(secretKey)

        return MerchantResponse(
            id = merchant.id,
            name = merchant.name,
            businessNumber = merchant.businessNumber,
            clientKey = merchant.clientKey,
            secretKey = merchant.secretKey,
        )
    }
}
