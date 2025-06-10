package com.goodpon.infra.persistence.account

import com.goodpon.goodpon.domain.account.Account
import com.goodpon.goodpon.domain.account.AccountRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class AccountCoreRepository(
    private val accountJpaRepository: AccountJpaRepository,
) : AccountRepository {

    override fun save(account: Account): Account {
        val accountEntity = AccountEntity.fromDomain(account)
        val savedAccountEntity = accountJpaRepository.save(accountEntity)

        return savedAccountEntity.toDomain()
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
