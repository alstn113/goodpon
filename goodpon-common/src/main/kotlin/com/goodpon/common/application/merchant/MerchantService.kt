package com.goodpon.common.application.merchant

import com.goodpon.common.application.merchant.request.MerchantCreateRequest
import com.goodpon.common.application.merchant.response.MerchantInfo
import com.goodpon.common.domain.merchant.MerchantReader
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
            businessNumber = merchant.businessNumber,
            secretKey = merchant.secretKey,
        )
    }

    @Transactional
    fun createMerchant(request: MerchantCreateRequest)
}
