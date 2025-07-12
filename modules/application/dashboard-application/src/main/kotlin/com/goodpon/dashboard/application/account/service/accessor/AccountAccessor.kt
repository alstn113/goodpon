package com.goodpon.dashboard.application.account.service.accessor

import com.goodpon.dashboard.application.account.port.out.AccountRepository
import com.goodpon.dashboard.application.account.port.out.exception.AccountNotFoundException
import com.goodpon.domain.account.Account
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class AccountAccessor(
    private val accountRepository: AccountRepository
) {

    @Transactional(readOnly = true)
    fun readById(id: Long): Account {
        return accountRepository.findById(id)
            ?: throw AccountNotFoundException()
    }

    @Transactional(readOnly = true)
    fun readByEmail(email: String): Account {
        return accountRepository.findByEmail(email)
            ?: throw AccountNotFoundException()
    }

    @Transactional(readOnly = true)
    fun existsByEmail(email: String): Boolean {
        return accountRepository.existsByEmail(email)
    }

    @Transactional
    fun create(account: Account): Account {
        return accountRepository.save(account)
    }

    @Transactional
    fun update(account: Account): Account {
        return accountRepository.save(account)
    }
}