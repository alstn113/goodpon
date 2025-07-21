package com.goodpon.application.dashboard.auth.service

import com.goodpon.application.dashboard.account.service.AccountVerificationService
import com.goodpon.application.dashboard.auth.port.`in`.VerifyEmailUseCase
import com.goodpon.application.dashboard.auth.port.out.EmailVerificationCache
import com.goodpon.application.dashboard.auth.service.exception.EmailVerificationNotFoundException
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.LocalDateTime

@Service
class VerifyEmailService(
    private val accountVerificationService: AccountVerificationService,
    private val emailVerificationCache: EmailVerificationCache,
    private val clock: Clock,
) : VerifyEmailUseCase {

    override fun invoke(token: String) {
        val now = LocalDateTime.now(clock)
        val verification = emailVerificationCache.findByToken(token)
            ?: throw EmailVerificationNotFoundException()

        accountVerificationService.verifyEmail(accountId = verification.accountId, verifiedAt = now)
        emailVerificationCache.delete(token = verification.token, accountId = verification.accountId)
    }
}