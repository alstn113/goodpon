package com.goodpon.core.application.account

import com.goodpon.core.domain.account.Account
import com.goodpon.core.domain.account.AccountRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class AccountUpdater(
    private val accountRepository: AccountRepository,
    private val accountReader: AccountReader,
) {
    @Transactional
    fun update(account: Account): Account {
        return accountRepository.save(account)
    }

    @Transactional
    fun verifyEmail(accountId: Long, verifiedAt: LocalDateTime) {
        val account = accountReader.readById(accountId)

        val verifiedAccount = account.verify(verifiedAt = verifiedAt)
        accountRepository.save(verifiedAccount)
    }
}