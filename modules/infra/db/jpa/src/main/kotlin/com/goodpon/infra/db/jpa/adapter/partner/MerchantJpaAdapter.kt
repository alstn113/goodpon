package com.goodpon.infra.db.jpa.adapter.partner

import com.goodpon.application.partner.merchant.port.out.MerchantRepository
import com.goodpon.domain.merchant.Merchant
import com.goodpon.infra.db.jpa.core.MerchantCoreRepository
import org.springframework.stereotype.Repository

@Repository("partnerMerchantJpaAdapter")
class MerchantJpaAdapter(
    private val merchantCoreRepository: MerchantCoreRepository,
) : MerchantRepository {

    override fun findByClientId(clientId: String): Merchant? {
        return merchantCoreRepository.finByClientId(clientId)
    }
}