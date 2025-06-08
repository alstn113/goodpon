package io.github.alstn113.goodpon.domain.auth

import io.github.alstn113.goodpon.domain.account.Account
import io.github.alstn113.goodpon.domain.account.AccountReader
import io.github.alstn113.goodpon.domain.account.AccountUpdater
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class EmailVerificationService(
    private val accountReader: AccountReader,
    private val accountUpdater: AccountUpdater,
    private val verificationTokenService: VerificationTokenService,
) {

    @Transactional(readOnly = true)
    fun validateResendRequest(email: String): Account {
        val account = accountReader.readByEmail(email)

        if (account.isNotPending()) {
            throw IllegalStateException("Account is already active")
        }

        return account
    }

    @Transactional
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