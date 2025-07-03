package com.goodpon.infra.jpa.account

import com.goodpon.core.domain.account.Account
import com.goodpon.core.domain.account.AccountRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class AccountCoreRepository(
    private val accountJpaRepository: AccountJpaRepository,
) : AccountRepository {
    override fun save(account: Account): Account {
        if (account.id == 0L) {
            val entity = AccountEntity.fromDomain(account)
            val savedEntity = accountJpaRepository.save(entity)
            return savedEntity.toDomain()
        }

        val entity = accountJpaRepository.findByIdOrNull(account.id)
            ?: throw IllegalArgumentException("Account with id ${account.id} not found")
        entity.update(account)
        val savedEntity = accountJpaRepository.save(entity)
        return savedEntity.toDomain()
    }

    override fun findById(id: Long): Account? {
        return accountJpaRepository.findByIdOrNull(id)
            ?.toDomain()
    }

    override fun findByEmail(email: String): Account? {
        return accountJpaRepository.findByEmail(email)
            ?.toDomain()
    }

    override fun existsByEmail(email: String): Boolean {
        return accountJpaRepository.existsByEmail(email)
    }
}
