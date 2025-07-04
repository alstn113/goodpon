package com.goodpon.core.application.account.accessor

import com.goodpon.core.application.account.exception.AccountNotFoundException
import com.goodpon.core.domain.account.Account
import com.goodpon.core.domain.account.AccountRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class AccountReader(
    private val accountRepository: AccountRepository,
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
}