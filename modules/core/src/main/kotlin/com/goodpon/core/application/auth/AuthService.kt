package com.goodpon.core.application.auth

import com.goodpon.core.application.auth.request.LoginRequest
import com.goodpon.core.application.auth.response.LoginResponse
import com.goodpon.core.domain.account.AccountReader
import com.goodpon.core.domain.account.AccountUpdater
import com.goodpon.core.domain.account.PasswordEncoder
import com.goodpon.core.domain.auth.EmailVerificationRepository
import com.goodpon.core.domain.auth.TokenProvider
import com.goodpon.core.domain.auth.VerificationEmailRequestedEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val accountReader: AccountReader,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: TokenProvider,
    private val emailVerificationRepository: EmailVerificationRepository,
    private val accountUpdater: AccountUpdater,
    private val eventPublisher: ApplicationEventPublisher,
) {

    @Transactional
    fun login(request: LoginRequest): LoginResponse {
        val account = accountReader.readByEmail(request.email)
        if (!passwordEncoder.matches(request.password, account.password)) {
            throw IllegalArgumentException("Invalid email or password")
        }

        val accessToken = tokenProvider.generateAccessToken(accountId = account.id)

        return LoginResponse(
            id = account.id,
            email = account.email,
            name = account.name,
            verified = account.verified,
            accessToken = accessToken,
        )
    }

    fun verifyEmail(token: String) {
        val verification = emailVerificationRepository.findByToken(token)
            ?: throw IllegalArgumentException("Invalid or expired verification token")

        accountUpdater.verifyEmail(verification.accountId)
        emailVerificationRepository.delete(token = token, accountId = verification.accountId)
    }

    fun resendVerificationEmail(email: String) {
        val account = accountReader.readByEmail(email)
        if (account.verified) {
            throw IllegalStateException("Account is already verified")
        }

        val event = VerificationEmailRequestedEvent(
            accountId = account.id,
            email = account.email,
            name = account.name
        )
        eventPublisher.publishEvent(event)
    }
}