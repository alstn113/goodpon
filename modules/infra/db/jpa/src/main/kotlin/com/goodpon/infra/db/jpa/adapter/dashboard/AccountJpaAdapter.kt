package com.goodpon.infra.db.jpa.adapter.dashboard

import com.goodpon.dashboard.application.account.port.out.AccountRepository
import com.goodpon.dashboard.application.account.port.out.exception.AccountNotFoundException
import com.goodpon.domain.account.Account
import com.goodpon.infra.db.jpa.core.AccountCoreRepository
import org.springframework.stereotype.Repository

@Repository("dashboardAccountJpaAdapter")
class AccountJpaAdapter(
    private val accountCoreRepository: AccountCoreRepository,
) : AccountRepository {

    override fun save(account: Account): Account {
        return accountCoreRepository.save(account)
            ?: throw AccountNotFoundException()
    }

    override fun findById(id: Long): Account? {
        return accountCoreRepository.findById(id)
    }

    override fun findByEmail(email: String): Account? {
        return accountCoreRepository.findByEmail(email)
    }

    override fun existsByEmail(email: String): Boolean {
        return accountCoreRepository.existsByEmail(email)
    }
}