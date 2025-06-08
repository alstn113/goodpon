package io.github.alstn113.goodpon.infra.persistence.merchant

import io.github.alstn113.goodpon.domain.merchant.Merchant
import io.github.alstn113.goodpon.domain.merchant.MerchantRepository
import org.springframework.stereotype.Repository

@Repository
class MerchantCoreRepository(
    private val merchantJpaRepository: MerchantJpaRepository,
) : MerchantRepository {

    override fun findByClientKey(clientKey: String): Merchant? {
        return merchantJpaRepository.findByClientKey(clientKey)?.let { entity ->
            Merchant(
                id = entity.id,
                name = entity.name,
                businessNumber = entity.businessNumber,
                clientKey = entity.clientKey,
                secretKey = entity.secretKey
            )
        }
    }

    override fun findBySecretKey(secretKey: String): Merchant? {
        return merchantJpaRepository.findBySecretKey(secretKey)?.let { entity ->
            Merchant(
                id = entity.id,
                name = entity.name,
                businessNumber = entity.businessNumber,
                clientKey = entity.clientKey,
                secretKey = entity.secretKey
            )
        }
    }
}
