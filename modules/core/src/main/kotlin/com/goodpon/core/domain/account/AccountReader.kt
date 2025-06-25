package com.goodpon.core.domain.account

import org.springframework.stereotype.Component

@Component
class AccountReader(
    private val accountRepository: AccountRepository,
) {

    fun readById(id: Long): Account {
        return accountRepository.findById(id)
            ?: throw IllegalArgumentException("Account with id $id not found")
    }

    fun readByEmail(email: String): Account {
        return accountRepository.findByEmail(email)
            ?: throw IllegalArgumentException("Account with email $email not found")
    }

    fun existsByEmail(email: String): Boolean {
        return accountRepository.existsByEmail(email)
    }
}