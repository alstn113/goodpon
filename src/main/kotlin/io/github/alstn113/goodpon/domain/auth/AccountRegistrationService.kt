package io.github.alstn113.goodpon.domain.auth

import io.github.alstn113.goodpon.domain.account.Account
import io.github.alstn113.goodpon.domain.account.AccountAppender
import io.github.alstn113.goodpon.domain.account.AccountReader
import io.github.alstn113.goodpon.domain.account.PasswordEncoder
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