package com.goodpon.infra.jpa.merchant.adapter

import com.goodpon.dashboard.application.merchant.port.out.MerchantAccountRepository
import com.goodpon.domain.merchant.MerchantAccount
import com.goodpon.domain.merchant.exception.MerchantAccountNotFoundException
import com.goodpon.infra.jpa.merchant.entity.MerchantAccountEntity
import com.goodpon.infra.jpa.merchant.repository.MerchantAccountJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class MerchantAccountJpaAdapter(
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