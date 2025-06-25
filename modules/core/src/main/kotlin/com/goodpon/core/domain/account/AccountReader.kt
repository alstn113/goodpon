package com.goodpon.core.domain.account

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class AccountReader(
    private val accountRepository: AccountRepository,
) {

    @Transactional(readOnly = true)
    fun readById(id: Long): Account {
        return accountRepository.findById(id)
            ?: throw IllegalArgumentException("Account with id $id not found")
    }

    @Transactional(readOnly = true)
    fun readByEmail(email: String): Account {
        return accountRepository.findByEmail(email)
            ?: throw IllegalArgumentException("Account with email $email not found")
    }

    @Transactional(readOnly = true)
    fun existsByEmail(email: String): Boolean {
        return accountRepository.existsByEmail(email)
    }
}