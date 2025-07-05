package com.goodpon.domain.application.account

import com.goodpon.domain.application.account.accessor.AccountReader
import com.goodpon.domain.application.account.accessor.AccountStore
import com.goodpon.domain.domain.account.Account
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
        val verifiedAccount = account.verify(verifiedAt = verifiedAt)

        return accountStore.update(verifiedAccount)
    }
}