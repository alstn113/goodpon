package com.goodpon.dashboard.application.auth.service

import com.goodpon.dashboard.application.account.service.AccountVerificationService
import com.goodpon.dashboard.application.auth.port.`in`.VerifyEmailUseCase
import com.goodpon.dashboard.application.auth.port.out.EmailVerificationCache
import com.goodpon.dashboard.application.auth.service.exception.EmailVerificationNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.LocalDateTime

@Service
class VerifyEmailService(
    private val accountVerificationService: AccountVerificationService,
    private val emailVerificationCache: EmailVerificationCache,
    private val clock: Clock,
) : VerifyEmailUseCase {

    @Transactional
    override fun verifyEmail(token: String) {
        val now = LocalDateTime.now(clock)
        val verification = emailVerificationCache.findByToken(token)
            ?: throw EmailVerificationNotFoundException()

        accountVerificationService.verifyEmail(accountId = verification.accountId, verifiedAt = now)
        emailVerificationCache.delete(token = verification.token, accountId = verification.accountId)
    }
}