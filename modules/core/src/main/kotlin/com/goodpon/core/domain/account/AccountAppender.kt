package com.goodpon.core.domain.account

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class AccountAppender(
    private val accountRepository: AccountRepository,
) {

    @Transactional
    fun append(account: Account): Account {
        return accountRepository.save(account)
    }
}