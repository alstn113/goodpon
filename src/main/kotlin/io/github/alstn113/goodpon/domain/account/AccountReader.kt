package io.github.alstn113.goodpon.domain.account

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class AccountReader(
    private val accountRepository: AccountRepository,
) {

    @Transactional(readOnly = true)
    fun existsByEmail(email: String): Boolean {
        return accountRepository.findByEmail(email) != null
    }
}