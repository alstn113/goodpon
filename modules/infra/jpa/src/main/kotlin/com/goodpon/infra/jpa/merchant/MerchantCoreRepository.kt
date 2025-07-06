package com.goodpon.infra.jpa.merchant

import com.goodpon.domain.merchant.Merchant
import com.goodpon.domain.merchant.exception.MerchantNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import com.goodpon.dashboard.application.merchant.port.out.MerchantRepository as Dashboard_MerchantRepository
import com.goodpon.partner.application.merchant.port.out.MerchantRepository as Partner_MerchantRepository

@Repository
class MerchantCoreRepository(
    private val merchantJpaRepository: MerchantJpaRepository,
) : Dashboard_MerchantRepository, Partner_MerchantRepository {

    override fun save(merchant: Merchant): Merchant {
        if (merchant.id == 0L) {
            val entity = MerchantEntity.fromDomain(merchant)
            val savedEntity = merchantJpaRepository.save(entity)
            return savedEntity.toDomain()
        }

        val entity = merchantJpaRepository.findByIdOrNull(merchant.id)
            ?: throw MerchantNotFoundException()
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
