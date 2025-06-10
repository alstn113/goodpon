package com.goodpon.domain.auth

import com.goodpon.goodpon.domain.account.Account
import com.goodpon.goodpon.domain.account.AccountAppender
import com.goodpon.goodpon.domain.account.AccountReader
import com.goodpon.goodpon.domain.account.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class AccountRegistrationService(
    private val accountReader: AccountReader,
    private val accountAppender: AccountAppender,
    private val passwordEncoder: PasswordEncoder,
) {

    fun registerAccount(email: String, password: String, name: String): Account {
        validateUniqueEmail(email)

        val hashedPassword = passwordEncoder.encode(password)
        val account = Account.create(email, hashedPassword, name)

        return accountAppender.append(account)
    }

    private fun validateUniqueEmail(email: String) {
        if (accountReader.existsByEmail(email)) {
            throw IllegalArgumentException("Email already exists")
        }
    }
}