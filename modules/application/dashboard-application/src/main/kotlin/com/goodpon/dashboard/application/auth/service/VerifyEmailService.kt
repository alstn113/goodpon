package com.goodpon.dashboard.application.auth.service

import com.goodpon.dashboard.application.account.service.AccountVerificationService
import com.goodpon.dashboard.application.auth.port.`in`.VerifyEmailUseCase
import com.goodpon.dashboard.application.auth.service.accessor.EmailVerificationReader
import com.goodpon.dashboard.application.auth.service.accessor.EmailVerificationStore
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class VerifyEmailService(
    private val accountVerificationService: AccountVerificationService,
    private val emailVerificationStore: EmailVerificationStore,
    private val emailVerificationReader: EmailVerificationReader,
) : VerifyEmailUseCase {

    @Transactional
    override fun verifyEmail(token: String) {
        val now = LocalDateTime.now()
        val verification = emailVerificationReader.readByToken(token)

        accountVerificationService.verifyEmail(accountId = verification.accountId, verifiedAt = now)
        emailVerificationStore.delete(token = token, accountId = verification.accountId)
    }
}