package com.goodpon.partner.application.merchant.service

import com.goodpon.partner.application.merchant.port.`in`.GetMerchantInfoUseCase
import com.goodpon.partner.application.merchant.port.`in`.dto.MerchantInfo
import com.goodpon.partner.application.merchant.service.accessor.MerchantReader
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MerchantService(
    private val merchantReader: MerchantReader,
) : GetMerchantInfoUseCase {

    @Transactional(readOnly = true)
    override fun getMerchantInfoBySecretKey(secretKey: String): MerchantInfo {
        val merchant = merchantReader.readBySecretKey(secretKey)

        return MerchantInfo(
            id = merchant.id,
            name = merchant.name,
            secretKey = merchant.secretKey,
        )
    }
}
