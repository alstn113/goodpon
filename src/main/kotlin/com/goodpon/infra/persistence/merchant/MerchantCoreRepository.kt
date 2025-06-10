package com.goodpon.infra.persistence.merchant

import com.goodpon.goodpon.domain.merchant.Merchant
import com.goodpon.goodpon.domain.merchant.MerchantRepository
import org.springframework.stereotype.Repository

@Repository
class MerchantCoreRepository(
    private val merchantJpaRepository: MerchantJpaRepository,
) : MerchantRepository {

    override fun findByClientKey(clientKey: String): Merchant? {
        return merchantJpaRepository.findByClientKey(clientKey)
            ?.toMerchant()
    }

    override fun findBySecretKey(secretKey: String): Merchant? {
        return merchantJpaRepository.findBySecretKey(secretKey)
            ?.toMerchant()
    }
}
