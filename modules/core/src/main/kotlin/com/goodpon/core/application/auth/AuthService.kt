package com.goodpon.core.application.auth

import com.goodpon.core.application.account.AccountVerificationService
import com.goodpon.core.application.account.accessor.AccountReader
import com.goodpon.core.application.auth.accessor.EmailVerificationReader
import com.goodpon.core.application.auth.accessor.EmailVerificationStore
import com.goodpon.core.application.auth.exception.PasswordMismatchException
import com.goodpon.core.application.auth.request.LoginRequest
import com.goodpon.core.application.auth.response.LoginResponse
import com.goodpon.core.domain.account.PasswordEncoder
import com.goodpon.core.domain.account.exception.AccountAlreadyVerifiedException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class AuthService(
    private val accountReader: AccountReader,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: TokenProvider,
    private val accountVerificationService: AccountVerificationService,
    private val emailVerificationStore: EmailVerificationStore,
    private val emailVerificationReader: EmailVerificationReader,
    private val eventPublisher: ApplicationEventPublisher,
) {
    @Transactional
    fun login(request: LoginRequest): LoginResponse {
        val account = accountReader.readByEmail(request.email)

        if (!passwordEncoder.matches(request.password, account.password.value)) {
            throw PasswordMismatchException()
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

    @Transactional
    fun verifyEmail(token: String) {
        val now = LocalDateTime.now()
        val verification = emailVerificationReader.readByToken(token)

        accountVerificationService.verifyEmail(accountId = verification.accountId, verifiedAt = now)
        emailVerificationStore.delete(token = token, accountId = verification.accountId)
    }

    @Transactional(readOnly = true)
    fun resendVerificationEmail(email: String) {
        val account = accountReader.readByEmail(email)
        if (account.verified) {
            throw AccountAlreadyVerifiedException()
        }

        val event = VerificationEmailRequestedEvent(
            accountId = account.id,
            email = account.email.value,
            name = account.name.value
        )
        eventPublisher.publishEvent(event)
    }
}
