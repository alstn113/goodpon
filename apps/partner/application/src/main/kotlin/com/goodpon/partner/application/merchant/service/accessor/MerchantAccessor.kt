package com.goodpon.partner.application.merchant.service.accessor

import com.goodpon.domain.merchant.Merchant
import com.goodpon.partner.application.merchant.port.out.MerchantRepository
import com.goodpon.partner.application.merchant.port.out.exception.MerchantNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class MerchantAccessor(
    private val merchantRepository: MerchantRepository,
) {

    @Transactional(readOnly = true)
    fun readByClientId(clientId: String): Merchant {
        return merchantRepository.findByClientId(clientId)
            ?: throw MerchantNotFoundException()
    }
}