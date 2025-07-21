package com.goodpon.application.partner.merchant.service

import com.goodpon.application.partner.merchant.port.`in`.AuthenticateMerchantUseCase
import com.goodpon.application.partner.merchant.port.`in`.dto.MerchantInfo
import com.goodpon.application.partner.merchant.service.accessor.MerchantAccessor
import com.goodpon.application.partner.merchant.service.exception.MerchantClientSecretMismatchException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthenticateMerchantService(
    private val merchantAccessor: MerchantAccessor,
) : AuthenticateMerchantUseCase {

    @Transactional(readOnly = true)
    override fun invoke(clientId: String, clientSecret: String): MerchantInfo {
        val merchant = merchantAccessor.readByClientId(clientId)

        if (!merchant.isValidClientSecret(clientSecret)) {
            throw MerchantClientSecretMismatchException()
        }

        return MerchantInfo(id = merchant.id, name = merchant.name)
    }
}
