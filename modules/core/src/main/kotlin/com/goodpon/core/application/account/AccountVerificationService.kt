package com.goodpon.core.application.account

import com.goodpon.core.application.account.accessor.AccountReader
import com.goodpon.core.application.account.accessor.AccountStore
import com.goodpon.core.domain.account.Account
import com.goodpon.core.domain.account.exception.AccountAlreadyVerifiedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class AccountVerificationService(
    private val accountReader: AccountReader,
    private val accountStore: AccountStore,
) {
    @Transactional
    fun verifyEmail(accountId: Long, verifiedAt: LocalDateTime): Account {
        val account = accountReader.readById(accountId)
        if (account.verified) {
            throw AccountAlreadyVerifiedException()
        }

        val verifiedAccount = account.verify(verifiedAt = verifiedAt)
        return accountStore.update(verifiedAccount)
    }
}