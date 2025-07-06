package com.goodpon.dashboard.application.merchant.service.accessor

import com.goodpon.dashboard.application.merchant.port.out.MerchantRepository
import com.goodpon.domain.merchant.Merchant
import com.goodpon.domain.merchant.exception.MerchantNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class MerchantReader(
    private val merchantRepository: MerchantRepository,
) {

    @Transactional(readOnly = true)
    fun readById(merchantId: Long): Merchant {
        return merchantRepository.findById(merchantId)
            ?: throw MerchantNotFoundException()
    }
}