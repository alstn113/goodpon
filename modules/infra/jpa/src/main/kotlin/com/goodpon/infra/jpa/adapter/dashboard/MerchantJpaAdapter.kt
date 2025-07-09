package com.goodpon.infra.jpa.adapter.dashboard

import com.goodpon.dashboard.application.merchant.port.out.MerchantRepository
import com.goodpon.domain.merchant.Merchant
import com.goodpon.infra.jpa.core.MerchantCoreRepository
import org.springframework.stereotype.Repository

@Repository
class MerchantJpaAdapter(
    private val merchantCoreRepository: MerchantCoreRepository,
) : MerchantRepository {

    override fun save(merchant: Merchant): Merchant {
        return merchantCoreRepository.save(merchant)
    }

    override fun findById(id: Long): Merchant? {
        return merchantCoreRepository.findById(id)
    }
}