package com.goodpon.infra.jpa.core

import com.goodpon.domain.merchant.Merchant
import com.goodpon.domain.merchant.exception.MerchantNotFoundException
import com.goodpon.infra.jpa.entity.MerchantEntity
import com.goodpon.infra.jpa.repository.MerchantJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class MerchantCoreRepository(
    private val merchantJpaRepository: MerchantJpaRepository,
) {

    fun save(merchant: Merchant): Merchant {
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

    fun findById(id: Long): Merchant? {
        return merchantJpaRepository.findByIdOrNull(id)
            ?.toDomain()
    }

    fun findBySecretKey(secretKey: String): Merchant? {
        return merchantJpaRepository.findBySecretKey(secretKey)
            ?.toDomain()
    }
}
