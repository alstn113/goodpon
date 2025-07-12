package com.goodpon.infra.db.jpa.adapter.dashboard

import com.goodpon.dashboard.application.merchant.port.out.MerchantRepository
import com.goodpon.dashboard.application.merchant.port.out.exception.MerchantNotFoundException
import com.goodpon.domain.merchant.Merchant
import com.goodpon.infra.db.jpa.core.MerchantCoreRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Repository

@Repository("dashboardMerchantJpaAdapter")
class MerchantJpaAdapter(
    private val merchantCoreRepository: MerchantCoreRepository,
) : MerchantRepository {

    override fun save(merchant: Merchant): Merchant {
        return try {
            merchantCoreRepository.save(merchant)
        } catch (e: EntityNotFoundException) {
            throw MerchantNotFoundException()
        }
    }

    override fun findById(id: Long): Merchant? {
        return merchantCoreRepository.findById(id)
    }
}