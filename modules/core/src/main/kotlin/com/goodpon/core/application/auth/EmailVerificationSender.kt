package com.goodpon.core.application.auth

import com.goodpon.core.domain.auth.*
import org.springframework.stereotype.Service

@Service
class EmailVerificationSender(
    private val emailSender: EmailSender,
    private val emailVerificationRepository: EmailVerificationRepository,
    private val verificationTokenGenerator: VerificationTokenGenerator,
    private val verificationLinkBuilder: VerificationLinkBuilder,
) {

    fun send(accountId: Long, email: String, name: String) {
        val token = verificationTokenGenerator.generate()
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