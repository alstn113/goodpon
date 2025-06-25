package com.goodpon.core.application.auth

import com.goodpon.core.domain.auth.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class EmailVerificationSender(
    private val emailSender: EmailSender,
    private val emailVerificationRepository: EmailVerificationRepository,
    private val verificationTokenGenerator: VerificationTokenGenerator,
    private val verificationLinkBuilder: VerificationLinkBuilder,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun send(accountId: Long, email: String, name: String) {
        val token = verificationTokenGenerator.generate()
        log.debug("이메일 인증 토큰 생성: {}", token)
        val emailVerification = EmailVerification.create(
            accountId = accountId,
            token = token,
            email = email,
            name = name
        )
        emailVerificationRepository.save(emailVerification)

        val verificationLink = verificationLinkBuilder.build(token)
        emailSender.sendVerificationEmail(name, email, verificationLink)
    }
}