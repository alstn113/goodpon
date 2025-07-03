package com.goodpon.core.application.auth

import com.goodpon.core.application.account.AccountReader
import com.goodpon.core.application.account.AccountUpdater
import com.goodpon.core.application.auth.request.LoginRequest
import com.goodpon.core.application.auth.response.LoginResponse
import com.goodpon.core.domain.account.PasswordEncoder
import com.goodpon.core.domain.auth.EmailVerificationRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

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
        if (!passwordEncoder.matches(request.password, account.password.value)) {
            throw IllegalArgumentException("Invalid email or password")
        }

        val accessToken = tokenProvider.generateAccessToken(accountId = account.id)

        return LoginResponse(
            id = account.id,
            email = account.email.value,
            name = account.name.value,
            verified = account.verified,
            accessToken = accessToken,
        )
    }

    fun verifyEmail(token: String) {
        val now = LocalDateTime.now()
        val verification = emailVerificationRepository.findByToken(token)
            ?: throw IllegalArgumentException("Invalid or expired verification token")

        accountUpdater.verifyEmail(
            accountId = verification.accountId,
            verifiedAt = now
        )
        emailVerificationRepository.delete(token = token, accountId = verification.accountId)
    }

    @Transactional(readOnly = true)
    fun resendVerificationEmail(email: String) {
        val account = accountReader.readByEmail(email)
        if (account.verified) {
            throw IllegalStateException("Account is already verified")
        }

        val event = VerificationEmailRequestedEvent(
            accountId = account.id,
            email = account.email.value,
            name = account.name.value
        )
        eventPublisher.publishEvent(event)
    }
}