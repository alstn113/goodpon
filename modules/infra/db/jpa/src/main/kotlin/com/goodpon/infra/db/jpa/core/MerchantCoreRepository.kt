package com.goodpon.infra.db.jpa.core

import com.goodpon.domain.merchant.Merchant
import com.goodpon.infra.db.jpa.core.EntityUtils.synchronizeEntityList
import com.goodpon.infra.db.jpa.entity.MerchantAccountEntity
import com.goodpon.infra.db.jpa.entity.MerchantClientSecretEntity
import com.goodpon.infra.db.jpa.entity.MerchantEntity
import com.goodpon.infra.db.jpa.repository.MerchantAccountJpaRepository
import com.goodpon.infra.db.jpa.repository.MerchantClientSecretJpaRepository
import com.goodpon.infra.db.jpa.repository.MerchantJpaRepository
import com.goodpon.infra.db.jpa.repository.dto.MyMerchantDetailDto
import com.goodpon.infra.db.jpa.repository.dto.MyMerchantSummaryDto
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class MerchantCoreRepository(
    private val merchantJpaRepository: MerchantJpaRepository,
    private val merchantAccountJpaRepository: MerchantAccountJpaRepository,
    private val merchantClientSecretJpaRepository: MerchantClientSecretJpaRepository,
) {

    @Transactional
    fun save(merchant: Merchant): Merchant {
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    fun finByClientId(clientId: String): Merchant? {
        val merchantEntity = merchantJpaRepository.findByClientId(clientId)
            ?: return null
        val accounts = merchantAccountJpaRepository.findByMerchantId(merchantEntity.id)
        val secrets = merchantClientSecretJpaRepository.findByMerchantId(merchantEntity.id)

        return merchantEntity.toDomain(
            accounts = accounts.map { it.toDomain() },
            secrets = secrets.map { it.toDomain() }
        )
    }

    @Transactional(readOnly = true)
    fun findMyMerchants(accountId: Long): List<MyMerchantSummaryDto> {
        return merchantJpaRepository.findMyMerchants(accountId)
    }

    @Transactional(readOnly = true)
    fun findMyMerchantDetailDto(accountId: Long, merchantId: Long): MyMerchantDetailDto? {
        val merchantSummary = merchantJpaRepository
            .findMyMerchantSummaryDto(merchantId, accountId) ?: return null
        val accounts = merchantAccountJpaRepository
            .findMyMerchantAccountDetailsDto(merchantId = merchantSummary.id, accountId = accountId)
        val secrets = merchantClientSecretJpaRepository
            .findMyMerchantClientSecretDetailsDto(merchantId = merchantSummary.id)

        return MyMerchantDetailDto(
            id = merchantSummary.id,
            name = merchantSummary.name,
            clientId = merchantSummary.clientId,
            merchantAccounts = accounts,
            clientSecrets = secrets,
            createdAt = merchantSummary.createdAt,
            updatedAt = merchantSummary.updatedAt
        )
    }
}
