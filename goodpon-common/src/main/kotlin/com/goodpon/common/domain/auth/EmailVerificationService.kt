package com.goodpon.common.domain.auth

import com.goodpon.common.domain.account.Account
import com.goodpon.common.domain.account.AccountReader
import com.goodpon.common.domain.account.AccountUpdater
import org.springframework.stereotype.Component

@Component
class EmailVerificationService(
    private val accountReader: AccountReader,
    private val accountUpdater: AccountUpdater,
    private val verificationTokenService: VerificationTokenService,
) {

    fun validateResendRequest(email: String): Account {
        val account = accountReader.readByEmail(email)

        if (account.isNotPending()) {
            throw IllegalStateException("Account is already active")
        }

        return account
    }

    fun verifyAccountEmail(token: String) {
        val accountId = verificationTokenService.validateAndConsumeToken(token)
        val account = accountReader.readById(accountId)

        if (account.isNotPending()) {
            throw IllegalStateException("Account is already active")
        }

        account.activate()
        accountUpdater.update(account)
    }
}