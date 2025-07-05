package com.goodpon.domain.application.account.accessor

import com.goodpon.domain.domain.account.Account
import com.goodpon.domain.domain.account.AccountRepository
import com.goodpon.domain.domain.account.exception.AccountNotFoundException
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