package com.goodpon.domain.account

import org.springframework.stereotype.Component

@Component
class AccountAppender(
    private val accountRepository: AccountRepository,
) {

    fun append(account: Account): Account {
        return accountRepository.save(account)
    }
}