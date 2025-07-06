package com.goodpon.dashboard.application.account.service.accessor

import com.goodpon.domain.account.Account
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