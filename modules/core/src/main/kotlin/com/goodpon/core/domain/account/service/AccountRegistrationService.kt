package com.goodpon.core.domain.account.service

import com.goodpon.core.domain.account.Account
import com.goodpon.core.domain.account.AccountRepository
import com.goodpon.core.domain.account.PasswordEncoder
import com.goodpon.core.support.error.CoreException
import com.goodpon.core.support.error.ErrorType
import org.springframework.stereotype.Component

@Component
class AccountRegistrationService(
    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    fun register(email: String, password: String, name: String): Account {
        validateUniqueEmail(email)

        val hashedPassword = passwordEncoder.encode(password)
        val account = Account.create(email, hashedPassword, name)

        return accountRepository.save(account)
    }

    private fun validateUniqueEmail(email: String) {
        if (accountRepository.existsByEmail(email)) {
            throw CoreException(ErrorType.ACCOUNT_EMAIL_ALREADY_EXISTS)
        }
    }
}