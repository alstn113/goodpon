package com.goodpon.partner.application.merchant.service

import com.goodpon.partner.application.merchant.service.accessor.MerchantReader
import com.goodpon.partner.application.merchant.service.response.MerchantInfo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MerchantService(
    private val merchantReader: MerchantReader,
) {

    @Transactional(readOnly = true)
    fun getMerchantInfoBySecretKey(secretKey: String): MerchantInfo {
        val merchant = merchantReader.readBySecretKey(secretKey)

        return MerchantInfo(
            id = merchant.id,
            name = merchant.name,
            secretKey = merchant.secretKey,
        )
    }
}
