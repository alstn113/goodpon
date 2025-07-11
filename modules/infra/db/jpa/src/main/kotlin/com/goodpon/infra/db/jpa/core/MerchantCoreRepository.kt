package com.goodpon.infra.db.jpa.core

import com.goodpon.domain.merchant.Merchant
import com.goodpon.infra.db.jpa.core.EntityUtils.synchronizeEntityList
import com.goodpon.infra.db.jpa.entity.MerchantAccountEntity
import com.goodpon.infra.db.jpa.entity.MerchantClientSecretEntity
import com.goodpon.infra.db.jpa.entity.MerchantEntity
import com.goodpon.infra.db.jpa.repository.MerchantAccountJpaRepository
import com.goodpon.infra.db.jpa.repository.MerchantClientSecretJpaRepository
import com.goodpon.infra.db.jpa.repository.MerchantJpaRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class MerchantCoreRepository(
    private val merchantJpaRepository: MerchantJpaRepository,
    private val merchantAccountJpaRepository: MerchantAccountJpaRepository,
    private val merchantClientSecretJpaRepository: MerchantClientSecretJpaRepository,
) {

    fun save(merchant: Merchant): Merchant? {
        val isNew = merchant.id == 0L

        val savedMerchantEntity = if (isNew) {
            val entity = MerchantEntity.fromDomain(merchant)
            merchantJpaRepository.save(entity)
        } else {
            val foundEntity = merchantJpaRepository.findByIdOrNull(merchant.id)
                ?: throw EntityNotFoundException()
            foundEntity.update(merchant)
            merchantJpaRepository.save(foundEntity)
        }

        val savedAccountEntities = synchronizeEntityList(
            isNew = isNew,
            existingEntities = merchantAccountJpaRepository.findByMerchantId(savedMerchantEntity.id),
            newDomains = merchant.accounts,
            idSelector = { it.id },
            entityIdSelector = { it.id },
            createEntity = { MerchantAccountEntity.fromDomain(it, savedMerchantEntity) },
            updateEntity = { entity, domain -> entity.update(domain) },
            saveAll = { merchantAccountJpaRepository.saveAll(it) },
            deleteAll = { merchantAccountJpaRepository.deleteAll(it) }
        )
        val savedSecretEntities = synchronizeEntityList(
            isNew = isNew,
            existingEntities = merchantClientSecretJpaRepository.findByMerchantId(savedMerchantEntity.id),
            newDomains = merchant.secrets,
            idSelector = { it.id },
            entityIdSelector = { it.id },
            createEntity = { MerchantClientSecretEntity.fromDomain(it, savedMerchantEntity) },
            updateEntity = { entity, domain -> entity.update(domain) },
            saveAll = { merchantClientSecretJpaRepository.saveAll(it) },
            deleteAll = { merchantClientSecretJpaRepository.deleteAll(it) }
        )

        return savedMerchantEntity.toDomain(
            accounts = savedAccountEntities.map { it.toDomain() },
            secrets = savedSecretEntities.map { it.toDomain() }
        )
    }

    fun findById(id: Long): Merchant? {
        val merchantEntity = merchantJpaRepository.findByIdOrNull(id)
            ?: return null
        val accounts = merchantAccountJpaRepository.findByMerchantId(id)
        val secrets = merchantClientSecretJpaRepository.findByMerchantId(id)

        return merchantEntity.toDomain(
            accounts = accounts.map { it.toDomain() },
            secrets = secrets.map { it.toDomain() }
        )
    }
}
