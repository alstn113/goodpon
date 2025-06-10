package com.goodpon.common.application.auth

import com.goodpon.common.domain.auth.VerificationTokenService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class VerificationEmailService(
    private val emailSender: EmailSender,
    private val verificationTokenService: VerificationTokenService,
    @Value("\${client-host}") private val baseUrl: String,
) {

    fun sendVerificationEmail(accountId: Long, email: String, name: String) {
        val token = verificationTokenService.generateToken(accountId)
        val verificationLink = buildVerificationLink(token)

        emailSender.sendVerificationEmail(name, email, verificationLink)
    }

    private fun buildVerificationLink(token: String): String {
        return "$baseUrl/verify?token=$token"
    }
}