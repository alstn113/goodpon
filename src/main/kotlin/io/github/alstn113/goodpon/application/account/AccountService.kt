package io.github.alstn113.goodpon.application.account

import io.github.alstn113.goodpon.domain.account.AccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService(
    private val accountRepository: AccountRepository,
) {
    @Transactional
    fun createAccount() {
    }
}
