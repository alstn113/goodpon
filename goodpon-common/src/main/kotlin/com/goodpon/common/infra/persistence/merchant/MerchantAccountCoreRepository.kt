package com.goodpon.common.infra.persistence.merchant

import com.goodpon.common.domain.merchant.MerchantAccount
import com.goodpon.common.domain.merchant.MerchantAccountRepository
import org.springframework.stereotype.Repository

@Repository
class MerchantAccountCoreRepository(
    private val merchantAccountJpaRepository: MerchantAccountJpaRepository,
) : MerchantAccountRepository {

    override fun save(merchantAccount: MerchantAccount): MerchantAccount {
        val entity = MerchantAccountEntity.fromDomain(merchantAccount)
        val savedEntity = merchantAccountJpaRepository.save(entity)

        return savedEntity.toDomain()
    }
}