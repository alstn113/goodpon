package com.goodpon.infra.db.jpa.core

import com.goodpon.domain.merchant.MerchantAccount
import com.goodpon.infra.db.jpa.entity.MerchantAccountEntity
import com.goodpon.infra.db.jpa.repository.MerchantAccountJpaRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class MerchantAccountCoreRepository(
    private val merchantAccountJpaRepository: MerchantAccountJpaRepository,
) {

    fun save(merchantAccount: MerchantAccount): MerchantAccount {
        if (merchantAccount.id == 0L) {
            val entity = MerchantAccountEntity.fromDomain(merchantAccount)
            val savedEntity = merchantAccountJpaRepository.save(entity)
            return savedEntity.toDomain()
        }

        val entity = merchantAccountJpaRepository.findByIdOrNull(merchantAccount.id)
            ?: throw EntityNotFoundException()
        entity.update(merchantAccount)
        val savedEntity = merchantAccountJpaRepository.save(entity)
        return savedEntity.toDomain()
    }

    fun findByMerchantIdAndAccountId(merchantId: Long, accountId: Long): MerchantAccount? {
        return merchantAccountJpaRepository.findByMerchantIdAndAccountId(merchantId, accountId)
            ?.toDomain()
    }
}