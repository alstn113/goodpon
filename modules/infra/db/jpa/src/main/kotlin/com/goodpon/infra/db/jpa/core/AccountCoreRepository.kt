package com.goodpon.infra.db.jpa.core

import com.goodpon.domain.account.Account
import com.goodpon.infra.db.jpa.entity.AccountEntity
import com.goodpon.infra.db.jpa.repository.AccountJpaRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class AccountCoreRepository(
    private val accountJpaRepository: AccountJpaRepository,
) {

    @Transactional
    fun save(account: Account): Account {
        if (account.id == 0L) {
            val entity = AccountEntity.fromDomain(account)
            val savedEntity = accountJpaRepository.save(entity)
            return savedEntity.toDomain()
        }

        val entity = accountJpaRepository.findByIdOrNull(account.id)
            ?: throw EntityNotFoundException()
        entity.update(account)
        val savedEntity = accountJpaRepository.save(entity)
        return savedEntity.toDomain()
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