package com.goodpon.dashboard.application.auth

import com.goodpon.dashboard.application.auth.service.EmailVerificationSender
import com.goodpon.dashboard.application.auth.service.VerificationLinkBuilder
import com.goodpon.dashboard.application.auth.service.VerificationTokenGenerator
import com.goodpon.domain.auth.EmailSender
import com.goodpon.domain.auth.EmailVerificationRepository
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class EmailVerificationSenderTest : DescribeSpec({

    val emailSender = mockk<EmailSender>(relaxed = true)
    val emailVerificationRepository = mockk<EmailVerificationRepository>()
    val verificationTokenGenerator: VerificationTokenGenerator = mockk()
    val verificationLinkBuilder: VerificationLinkBuilder = mockk()
    val emailVerificationSender = EmailVerificationSender(
        emailSender,
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

            emailVerificationSender.send(accountId, email, name)

            verify { verificationTokenGenerator.generate() }
            verify { verificationLinkBuilder.build(token) }
            verify { emailVerificationRepository.save(any()) }
            verify { emailSender.sendVerificationEmail(name, email, verificationLink) }
        }
    }
})