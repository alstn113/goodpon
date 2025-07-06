package com.goodpon.partner.application.account.accessor

import com.goodpon.domain.account.Account
import com.goodpon.domain.account.AccountRepository
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