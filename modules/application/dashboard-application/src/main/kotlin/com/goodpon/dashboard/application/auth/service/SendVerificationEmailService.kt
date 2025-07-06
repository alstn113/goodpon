package com.goodpon.dashboard.application.auth.service

import com.goodpon.dashboard.application.auth.port.out.EmailVerificationRepository
import com.goodpon.dashboard.application.auth.port.out.VerificationEmailSender
import com.goodpon.dashboard.application.auth.port.out.dto.SendVerificationEmailRequest
import com.goodpon.domain.auth.EmailVerification
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SendVerificationEmailService(
    private val verificationEmailSender: VerificationEmailSender,
    private val emailVerificationRepository: EmailVerificationRepository,
    private val verificationTokenGenerator: VerificationTokenGenerator,
    private val verificationLinkBuilder: VerificationLinkBuilder,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun send(accountId: Long, email: String, name: String) {
        val now = LocalDateTime.now()
        val token = verificationTokenGenerator.generate()

        log.debug("이메일 인증 토큰 생성: {}", token)

        val emailVerification = EmailVerification(
            accountId = accountId,
            token = token,
            email = email,
            name = name,
            createdAt = now
        )
        emailVerificationRepository.save(emailVerification)

        val verificationLink = verificationLinkBuilder.build(token)

        val request = SendVerificationEmailRequest(name, email, verificationLink)
        verificationEmailSender.send(request)
    }
}