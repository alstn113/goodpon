package com.goodpon.core.domain.account

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

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
    fun verifyEmail(accountId: Long) {
        val account = accountReader.readById(accountId)

        val verifiedAccount = account.verify()
        accountRepository.save(verifiedAccount)
    }
}