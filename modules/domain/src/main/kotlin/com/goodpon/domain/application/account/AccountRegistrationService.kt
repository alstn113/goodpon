package com.goodpon.domain.application.account

import com.goodpon.domain.application.account.accessor.AccountReader
import com.goodpon.domain.application.account.accessor.AccountStore
import com.goodpon.domain.application.account.exception.AccountEmailExistsException
import com.goodpon.domain.domain.account.Account
import com.goodpon.domain.application.auth.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountRegistrationService(
    private val accountReader: AccountReader,
    private val accountStore: AccountStore,
    private val passwordEncoder: PasswordEncoder,
) {

    @Transactional
    fun register(email: String, password: String, name: String): Account {
        validateUniqueEmail(email)

        val hashedPassword = passwordEncoder.encode(password)
        val account = Account.create(email, hashedPassword, name)

        return accountStore.create(account)
    }

    private fun validateUniqueEmail(email: String) {
        if (accountReader.existsByEmail(email)) {
            throw AccountEmailExistsException()
        }
    }
}