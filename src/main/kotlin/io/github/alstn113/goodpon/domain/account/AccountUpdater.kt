package io.github.alstn113.goodpon.domain.account

import org.springframework.stereotype.Component

@Component
class AccountUpdater(
    private val accountRepository: AccountRepository,
) {

    fun update(account: Account): Account {
        return accountRepository.save(account)
    }
}