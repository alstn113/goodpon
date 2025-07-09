package com.goodpon.infra.jpa.core

import com.goodpon.domain.account.Account
import com.goodpon.infra.jpa.entity.AccountEntity
import com.goodpon.infra.jpa.repository.AccountJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class AccountCoreRepository(
    private val accountJpaRepository: AccountJpaRepository,
) {

    fun save(account: Account): Account? {
        if (account.id == 0L) {
            val entity = AccountEntity.fromDomain(account)
            val savedEntity = accountJpaRepository.save(entity)
            return savedEntity.toDomain()
        }

        val entity = accountJpaRepository.findByIdOrNull(account.id)
            ?: return null
        entity.update(account)
        val savedEntity = accountJpaRepository.save(entity)
        return savedEntity.toDomain()
    }

    fun findById(id: Long): Account? {
        return accountJpaRepository.findByIdOrNull(id)
            ?.toDomain()
    }

    fun findByEmail(email: String): Account? {
        return accountJpaRepository.findByEmail(email)
            ?.toDomain()
    }

    fun existsByEmail(email: String): Boolean {
        return accountJpaRepository.existsByEmail(email)
    }
}