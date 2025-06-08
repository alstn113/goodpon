package io.github.alstn113.goodpon.domain.account

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class AccountReader(
    private val accountRepository: AccountRepository,
) {

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