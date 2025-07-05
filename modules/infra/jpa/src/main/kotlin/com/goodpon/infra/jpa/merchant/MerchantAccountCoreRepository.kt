package com.goodpon.infra.jpa.merchant

import com.goodpon.core.domain.merchant.MerchantAccount
import com.goodpon.core.domain.merchant.MerchantAccountRepository
import com.goodpon.core.domain.merchant.exception.MerchantAccountNotFoundException
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
            ?: throw MerchantAccountNotFoundException()
        entity.update(merchantAccount)
        val savedEntity = merchantAccountJpaRepository.save(entity)
        return savedEntity.toDomain()
    }

    override fun findByMerchantIdAndAccountId(merchantId: Long, accountId: Long): MerchantAccount? {
        return merchantAccountJpaRepository.findByMerchantIdAndAccountId(merchantId, accountId)
            ?.toDomain()
    }
}