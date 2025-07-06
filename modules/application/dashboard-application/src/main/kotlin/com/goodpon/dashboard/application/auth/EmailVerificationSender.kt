package com.goodpon.dashboard.application.auth

import com.goodpon.domain.auth.EmailSender
import com.goodpon.domain.auth.EmailVerification
import com.goodpon.domain.auth.EmailVerificationRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class EmailVerificationSender(
    private val emailSender: EmailSender,
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
        emailSender.sendVerificationEmail(name, email, verificationLink)
    }
}