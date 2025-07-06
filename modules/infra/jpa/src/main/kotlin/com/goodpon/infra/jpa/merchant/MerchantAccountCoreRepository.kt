package com.goodpon.infra.jpa.merchant

import com.goodpon.domain.merchant.MerchantAccountRepository
import com.goodpon.domain.merchant.exception.MerchantAccountNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class MerchantAccountCoreRepository(
    private val merchantAccountJpaRepository: MerchantAccountJpaRepository,
) : MerchantAccountRepository {

    override fun save(merchantAccount: com.goodpon.domain.merchant.MerchantAccount): com.goodpon.domain.merchant.MerchantAccount {
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

    override fun findByMerchantIdAndAccountId(
        merchantId: Long,
        accountId: Long,
    ): com.goodpon.domain.merchant.MerchantAccount? {
        return merchantAccountJpaRepository.findByMerchantIdAndAccountId(merchantId, accountId)
            ?.toDomain()
    }
}