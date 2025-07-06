package com.goodpon.dashboard.application.auth

import com.goodpon.dashboard.application.auth.service.SendVerificationEmailService
import com.goodpon.dashboard.application.auth.service.VerificationLinkBuilder
import com.goodpon.dashboard.application.auth.service.VerificationTokenGenerator
import com.goodpon.dashboard.application.auth.port.out.SendVerificationEmailPort
import com.goodpon.dashboard.application.auth.port.out.EmailVerificationRepository
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class EmailVerificationSenderTest : DescribeSpec({

    val sendVerificationEmailPort = mockk<SendVerificationEmailPort>(relaxed = true)
    val emailVerificationRepository = mockk<EmailVerificationRepository>()
    val verificationTokenGenerator: VerificationTokenGenerator = mockk()
    val verificationLinkBuilder: VerificationLinkBuilder = mockk()
    val sendVerificationEmailService = SendVerificationEmailService(
        sendVerificationEmailPort,
        emailVerificationRepository,
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
            every { emailVerificationRepository.save(any()) } returns Unit

            sendVerificationEmailService.send(accountId, email, name)

            verify { verificationTokenGenerator.generate() }
            verify { verificationLinkBuilder.build(token) }
            verify { emailVerificationRepository.save(any()) }
            verify { sendVerificationEmailPort.send(name, email, verificationLink) }
        }
    }
})