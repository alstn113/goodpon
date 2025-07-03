package com.goodpon.infra.jpa.merchant

import com.goodpon.core.domain.merchant.Merchant
import com.goodpon.core.domain.merchant.MerchantRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class MerchantCoreRepository(
    private val merchantJpaRepository: MerchantJpaRepository,
) : MerchantRepository {
    override fun save(merchant: Merchant): Merchant {
        if (merchant.id == 0L) {
            val entity = MerchantEntity.fromDomain(merchant)
            val savedEntity = merchantJpaRepository.save(entity)
            return savedEntity.toDomain()
        }

        val entity = merchantJpaRepository.findByIdOrNull(merchant.id)
            ?: throw IllegalArgumentException("Merchant with id ${merchant.id} not found")
        entity.update(merchant)
        val savedEntity = merchantJpaRepository.save(entity)
        return savedEntity.toDomain()
    }

    override fun findById(id: Long): Merchant? {
        return merchantJpaRepository.findByIdOrNull(id)
            ?.toDomain()
    }

    override fun findBySecretKey(secretKey: String): Merchant? {
        return merchantJpaRepository.findBySecretKey(secretKey)
            ?.toDomain()
    }
}
