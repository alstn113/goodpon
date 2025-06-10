package com.goodpon.common.infra.persistence.merchant

import com.goodpon.common.domain.merchant.Merchant
import com.goodpon.common.domain.merchant.MerchantRepository
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
