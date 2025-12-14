package com.goodpon.infra.jpa.core

import com.goodpon.domain.account.Account
import com.goodpon.infra.jpa.entity.AccountEntity
import com.goodpon.infra.jpa.repository.AccountJpaRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class AccountCoreRepository(
    private val accountJpaRepository: AccountJpaRepository,
    private val em: EntityManager,
) {

    @Transactional
    fun save(account: Account): Account {
        if (account.id == 0L) {
            val entity = AccountEntity.fromDomain(account)
            em.persist(entity)
            return entity.toDomain()
        }

        val entity = accountJpaRepository.findByIdOrNull(account.id)
            ?: throw EntityNotFoundException()
        entity.update(account)
        return entity.toDomain()
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): Account? {
        return accountJpaRepository.findByIdOrNull(id)
            ?.toDomain()
    }

    @Transactional(readOnly = true)
    fun findByEmail(email: String): Account? {
        return accountJpaRepository.findByEmail(email)
            ?.toDomain()
    }

    @Transactional(readOnly = true)
    fun existsByEmail(email: String): Boolean {
        return accountJpaRepository.existsByEmail(email)
    }
}