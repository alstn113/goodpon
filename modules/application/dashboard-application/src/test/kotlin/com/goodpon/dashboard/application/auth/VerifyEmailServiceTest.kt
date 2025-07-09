package com.goodpon.dashboard.application.auth

import com.goodpon.dashboard.application.account.service.AccountVerificationService
import com.goodpon.dashboard.application.auth.port.out.EmailVerificationCache
import com.goodpon.dashboard.application.auth.service.VerifyEmailService
import com.goodpon.dashboard.application.auth.service.exception.EmailVerificationNotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk

class VerifyEmailServiceTest : DescribeSpec({

    val accountVerificationService = mockk<AccountVerificationService>()
    val emailVerificationCache = mockk<EmailVerificationCache>()
    val verifyEmailService = VerifyEmailService(accountVerificationService, emailVerificationCache)

    describe("verifyEmail") {
        it("토큰으로 이메일 인증 정보를 찾지 못할 경우 예외를 발생시킨다.") {
            val invalidToken = "invalid-token"
            every {
                emailVerificationCache.findByToken(invalidToken)
            } returns null

            shouldThrow<EmailVerificationNotFoundException> {
                verifyEmailService.verifyEmail(invalidToken)
            }
        }
    }
})
