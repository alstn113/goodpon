package com.goodpon.dashboard.application.auth

import com.goodpon.dashboard.application.account.service.AccountVerificationService
import com.goodpon.dashboard.application.auth.port.out.EmailVerificationCache
import com.goodpon.dashboard.application.auth.port.out.dto.EmailVerificationDto
import com.goodpon.dashboard.application.auth.service.VerifyEmailService
import com.goodpon.dashboard.application.auth.service.exception.EmailVerificationNotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId

class VerifyEmailServiceTest : DescribeSpec({

    val accountVerificationService = mockk<AccountVerificationService>()
    val emailVerificationCache = mockk<EmailVerificationCache>()
    val clock = mockk<Clock>()
    val verifyEmailService = VerifyEmailService(accountVerificationService, emailVerificationCache, clock)

    val now = LocalDateTime.now()
    val zone = ZoneId.systemDefault()
    val instant = now.atZone(zone).toInstant()

    beforeTest {
        clearAllMocks()
        every { clock.instant() } returns instant
        every { clock.zone } returns zone
    }

    describe("verifyEmail") {
        it("이메일 인증 처리를 정상적으로 수행한다") {
            val token = "valid-token"


            val emailVerificationDto = EmailVerificationDto(
                accountId = 1L,
                token = token,
                email = "test@goodpon.site",
                name = "Test User",
                createdAt = now
            )

            every { clock.instant() } returns instant
            every { clock.zone } returns zone

            every { emailVerificationCache.findByToken(token) } returns emailVerificationDto
            every { accountVerificationService.verifyEmail(emailVerificationDto.accountId, now) } returns mockk()
            every { emailVerificationCache.delete(token, accountId = emailVerificationDto.accountId) } returns Unit

            verifyEmailService(token)

            verify { emailVerificationCache.findByToken(token) }
            verify { accountVerificationService.verifyEmail(emailVerificationDto.accountId, now) }
            verify { emailVerificationCache.delete(token, accountId = emailVerificationDto.accountId) }
        }

        it("토큰으로 이메일 인증 정보를 찾지 못할 경우 예외를 발생시킨다.") {
            val invalidToken = "invalid-token"
            every {
                emailVerificationCache.findByToken(invalidToken)
            } returns null

            shouldThrow<EmailVerificationNotFoundException> {
                verifyEmailService(invalidToken)
            }
        }
    }
})
