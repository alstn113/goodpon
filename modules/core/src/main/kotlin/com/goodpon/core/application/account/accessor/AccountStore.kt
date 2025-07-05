package com.goodpon.core.application.account.accessor

import com.goodpon.core.domain.account.Account
import com.goodpon.core.domain.account.AccountRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class AccountStore(
    private val accountRepository: AccountRepository,
) {

    @Transactional
    fun create(account: Account): Account {
        return accountRepository.save(account)
    }

    @Transactional
    fun update(account: Account): Account {
        return accountRepository.save(account)
    }
}