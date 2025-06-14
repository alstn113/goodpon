package com.goodpon.core.domain.auth

import com.goodpon.core.domain.account.Account
import com.goodpon.core.domain.account.AccountReader
import com.goodpon.core.domain.account.AccountUpdater
import org.springframework.stereotype.Component

@Component
class EmailVerificationService(
    private val accountReader: AccountReader,
    private val accountUpdater: AccountUpdater,
    private val verificationTokenService: VerificationTokenService,
) {

    fun validateResendRequest(email: String): Account {
        val account = accountReader.readByEmail(email)

        if (account.isNotUnverified()) {
            throw IllegalStateException("Account is already verified")
        }

        return account
    }

    fun verifyAccountEmail(token: String) {
        val accountId = verificationTokenService.validateAndConsumeToken(token)
        val account = accountReader.readById(accountId)

        if (account.isNotUnverified()) {
            throw IllegalStateException("Account is already verified")
        }

        account.verify()
        accountUpdater.update(account)
    }
}