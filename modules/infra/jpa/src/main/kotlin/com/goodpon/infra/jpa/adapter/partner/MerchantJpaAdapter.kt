package com.goodpon.infra.jpa.adapter.partner

import com.goodpon.domain.merchant.Merchant
import com.goodpon.infra.jpa.core.MerchantCoreRepository
import com.goodpon.partner.application.merchant.port.out.MerchantRepository
import org.springframework.stereotype.Repository

@Repository
class MerchantJpaAdapter(
    private val merchantCoreRepository: MerchantCoreRepository,
) : MerchantRepository {

    override fun findBySecretKey(secretKey: String): Merchant? {
        return merchantCoreRepository.findBySecretKey(secretKey)
    }
}