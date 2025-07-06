package com.goodpon.partner.application.merchant.service.accessor

import com.goodpon.domain.merchant.Merchant
import com.goodpon.domain.merchant.exception.MerchantNotFoundException
import com.goodpon.partner.application.merchant.port.out.MerchantRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class MerchantReader(
    private val merchantRepository: MerchantRepository,
) {

    @Transactional(readOnly = true)
    fun readBySecretKey(secretKey: String): Merchant {
        return merchantRepository.findBySecretKey(secretKey)
            ?: throw MerchantNotFoundException()
    }
}