package com.goodpon.dashboard.application.account.service

import com.goodpon.dashboard.application.account.service.accessor.AccountAccessor
import com.goodpon.dashboard.application.account.service.exception.AccountEmailExistsException
import com.goodpon.dashboard.application.auth.port.out.PasswordEncoder
import com.goodpon.domain.account.Account
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountRegistrationService(
    private val accountAccessor: AccountAccessor,
    private val passwordEncoder: PasswordEncoder,
) {

    @Transactional
    fun register(email: String, password: String, name: String): Account {
        validateUniqueEmail(email)

        val hashedPassword = passwordEncoder.encode(password)
        val account = Account.create(email, hashedPassword, name)

        return accountAccessor.create(account)
    }

    private fun validateUniqueEmail(email: String) {
        if (accountAccessor.existsByEmail(email)) {
            throw AccountEmailExistsException()
        }
    }
}