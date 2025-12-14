package com.goodpon.partner.infra.persistence

import com.goodpon.domain.merchant.Merchant
import com.goodpon.infra.jpa.core.MerchantCoreRepository
import com.goodpon.partner.application.merchant.port.out.MerchantRepository
import org.springframework.stereotype.Repository

@Repository
class MerchantJpaAdapter(
    private val merchantCoreRepository: MerchantCoreRepository,
) : MerchantRepository {

    override fun findByClientId(clientId: String): Merchant? {
        return merchantCoreRepository.finByClientId(clientId)
    }
}