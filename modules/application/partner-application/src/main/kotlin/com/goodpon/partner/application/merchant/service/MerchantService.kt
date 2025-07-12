package com.goodpon.partner.application.merchant.service

import com.goodpon.partner.application.merchant.port.`in`.AuthenticateMerchantUseCase
import com.goodpon.partner.application.merchant.port.`in`.dto.MerchantInfo
import com.goodpon.partner.application.merchant.service.accessor.MerchantReader
import com.goodpon.partner.application.merchant.service.exception.MerchantClientSecretMismatchException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MerchantService(
    private val merchantReader: MerchantReader,
) : AuthenticateMerchantUseCase {

    @Transactional(readOnly = true)
    override fun authenticate(clientId: String, clientSecret: String): MerchantInfo {
        val merchant = merchantReader.readByClientId(clientId)

        if (!merchant.hasValidClientSecret(clientSecret)) {
            throw MerchantClientSecretMismatchException()
        }

        return MerchantInfo(
            id = merchant.id,
            name = merchant.name,
        )
    }
}
