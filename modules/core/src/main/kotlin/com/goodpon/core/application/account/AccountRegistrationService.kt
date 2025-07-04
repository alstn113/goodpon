package com.goodpon.core.application.account

import com.goodpon.core.application.account.accessor.AccountReader
import com.goodpon.core.application.account.accessor.AccountStore
import com.goodpon.core.application.account.exception.AccountEmailExistsException
import com.goodpon.core.domain.account.Account
import com.goodpon.core.domain.account.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class AccountRegistrationService(
    private val accountReader: AccountReader,
    private val accountStore: AccountStore,
    private val passwordEncoder: PasswordEncoder,
) {
    fun register(email: String, password: String, name: String): Account {
        validateUniqueEmail(email)

        val hashedPassword = passwordEncoder.encode(password)
        val account = Account.create(email, hashedPassword, name)

        return accountStore.createAccount(account)
    }

    private fun validateUniqueEmail(email: String) {
        if (accountReader.existsByEmail(email)) {
            throw AccountEmailExistsException()
        }
    }
}