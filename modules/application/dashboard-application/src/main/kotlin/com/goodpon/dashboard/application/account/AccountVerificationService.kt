package com.goodpon.dashboard.application.account

import com.goodpon.domain.account.Account
import com.goodpon.dashboard.application.account.accessor.AccountReader
import com.goodpon.dashboard.application.account.accessor.AccountStore
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