package com.goodpon.dashboard.application.account.service

import com.goodpon.dashboard.application.account.service.accessor.AccountAccessor
import com.goodpon.domain.account.Account
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class AccountVerificationService(
    private val accountAccessor: AccountAccessor,
) {

    @Transactional
    fun verifyEmail(accountId: Long, verifiedAt: LocalDateTime): Account {
        val account = accountAccessor.readById(accountId)
        val verifiedAccount = account.verify(verifiedAt = verifiedAt)

        return accountAccessor.update(verifiedAccount)
    }
}