package com.goodpon.infra.jpa.merchant

import com.goodpon.core.domain.merchant.MerchantAccount
import com.goodpon.core.domain.merchant.MerchantAccountRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class MerchantAccountCoreRepository(
    private val merchantAccountJpaRepository: MerchantAccountJpaRepository,
) : MerchantAccountRepository {

    override fun save(merchantAccount: MerchantAccount): MerchantAccount {
        if (merchantAccount.id == 0L) {
            val entity = MerchantAccountEntity.fromDomain(merchantAccount)
            val savedEntity = merchantAccountJpaRepository.save(entity)
            return savedEntity.toDomain()
        }

        val entity = merchantAccountJpaRepository.findByIdOrNull(merchantAccount.id)
            ?: throw IllegalArgumentException("MerchantAccount with id ${merchantAccount.id} not found")
        entity.update(merchantAccount)
        val savedEntity = merchantAccountJpaRepository.save(entity)
        return savedEntity.toDomain()
    }
}