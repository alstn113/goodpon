package com.goodpon.application.dashboard.auth.service

import com.goodpon.application.dashboard.auth.port.out.EmailVerificationCache
import com.goodpon.application.dashboard.auth.port.out.VerificationEmailSender
import com.goodpon.application.dashboard.auth.port.out.dto.EmailVerificationDto
import com.goodpon.application.dashboard.auth.port.out.dto.SendVerificationEmailRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SendVerificationEmailService(
    private val verificationEmailSender: VerificationEmailSender,
    private val emailVerificationCache: EmailVerificationCache,
    private val verificationTokenGenerator: VerificationTokenGenerator,
    private val verificationLinkBuilder: VerificationLinkBuilder,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun send(accountId: Long, email: String, name: String) {
        val now = LocalDateTime.now()
        val token = verificationTokenGenerator.generate()

        log.debug("이메일 인증 토큰 생성: {}", token)

        val emailVerification = EmailVerificationDto(
            accountId = accountId,
            token = token,
            email = email,
            name = name,
            createdAt = now
        )
        emailVerificationCache.save(emailVerification)

        val verificationLink = verificationLinkBuilder.build(token)

        val request = SendVerificationEmailRequest(name, email, verificationLink)
        verificationEmailSender.send(request)
    }
}