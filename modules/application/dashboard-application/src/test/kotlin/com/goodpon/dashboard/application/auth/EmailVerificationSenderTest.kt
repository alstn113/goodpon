package com.goodpon.dashboard.application.auth

import com.goodpon.dashboard.application.auth.port.out.EmailVerificationCache
import com.goodpon.dashboard.application.auth.port.out.VerificationEmailSender
import com.goodpon.dashboard.application.auth.port.out.dto.SendVerificationEmailRequest
import com.goodpon.dashboard.application.auth.service.SendVerificationEmailService
import com.goodpon.dashboard.application.auth.service.VerificationLinkBuilder
import com.goodpon.dashboard.application.auth.service.VerificationTokenGenerator
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class EmailVerificationSenderTest : DescribeSpec({

    val verificationEmailSender = mockk<VerificationEmailSender>(relaxed = true)
    val emailVerificationCache = mockk<EmailVerificationCache>()
    val verificationTokenGenerator: VerificationTokenGenerator = mockk()
    val verificationLinkBuilder: VerificationLinkBuilder = mockk()
    val sendVerificationEmailService = SendVerificationEmailService(
        verificationEmailSender,
        emailVerificationCache,
        verificationTokenGenerator,
        verificationLinkBuilder
    )

    beforeEach {
        clearAllMocks()
    }

    describe("send") {
        val accountId = 1L
        val email = "email@goodpon.site"
        val name = "name"
        val token = "generated-token"
        val verificationLink = "https://goodpon.site/verify?token=$token"


        it("토큰을 생성하여 저장하고, 토큰이 담긴 링크를 이메일로 전송한다.") {
            every { verificationTokenGenerator.generate() } returns token
            every { verificationLinkBuilder.build(token) } returns verificationLink
            every { emailVerificationCache.save(any()) } returns Unit

            sendVerificationEmailService.send(accountId, email, name)

            verify { verificationTokenGenerator.generate() }
            verify { verificationLinkBuilder.build(token) }
            verify { emailVerificationCache.save(any()) }
            verify {
                verificationEmailSender.send(
                    SendVerificationEmailRequest(
                        name = name,
                        email = email,
                        verificationLink = verificationLink
                    )
                )
            }
        }
    }
})