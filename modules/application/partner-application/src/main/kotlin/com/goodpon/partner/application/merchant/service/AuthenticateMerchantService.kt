package com.goodpon.partner.application.merchant.service

import com.goodpon.partner.application.merchant.port.`in`.AuthenticateMerchantUseCase
import com.goodpon.partner.application.merchant.port.`in`.dto.MerchantInfo
import com.goodpon.partner.application.merchant.service.accessor.MerchantAccessor
import com.goodpon.partner.application.merchant.service.exception.MerchantClientSecretMismatchException
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
