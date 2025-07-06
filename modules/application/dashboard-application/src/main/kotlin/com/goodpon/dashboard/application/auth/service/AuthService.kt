package com.goodpon.dashboard.application.auth.service

import com.goodpon.dashboard.application.account.service.AccountVerificationService
import com.goodpon.dashboard.application.account.service.accessor.AccountReader
import com.goodpon.dashboard.application.auth.port.`in`.LoginUseCase
import com.goodpon.dashboard.application.auth.port.`in`.ResendVerificationEmailUseCase
import com.goodpon.dashboard.application.auth.port.`in`.VerifyEmailUseCase
import com.goodpon.dashboard.application.auth.port.`in`.dto.LoginCommand
import com.goodpon.dashboard.application.auth.port.`in`.dto.LoginResult
import com.goodpon.dashboard.application.auth.port.out.PasswordEncoder
import com.goodpon.dashboard.application.auth.port.out.TokenProvider
import com.goodpon.dashboard.application.auth.service.accessor.EmailVerificationReader
import com.goodpon.dashboard.application.auth.service.accessor.EmailVerificationStore
import com.goodpon.dashboard.application.auth.service.event.ResendVerificationEmailEvent
import com.goodpon.dashboard.application.auth.service.exception.PasswordMismatchException
import com.goodpon.domain.account.exception.AccountAlreadyVerifiedException
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
) : LoginUseCase, VerifyEmailUseCase, ResendVerificationEmailUseCase {

    @Transactional
    override fun login(command: LoginCommand): LoginResult {
        val account = accountReader.readByEmail(command.email)

        if (!passwordEncoder.matches(command.password, account.password.value)) {
            throw PasswordMismatchException()
        }

        val accessToken = tokenProvider.generateAccessToken(accountId = account.id)

        return LoginResult(
            id = account.id,
            email = account.email.value,
            name = account.name.value,
            verified = account.verified,
            accessToken = accessToken,
        )
    }

    @Transactional
    override fun verifyEmail(token: String) {
        val now = LocalDateTime.now()
        val verification = emailVerificationReader.readByToken(token)

        accountVerificationService.verifyEmail(accountId = verification.accountId, verifiedAt = now)
        emailVerificationStore.delete(token = token, accountId = verification.accountId)
    }

    @Transactional(readOnly = true)
    override fun resendVerificationEmail(email: String) {
        val account = accountReader.readByEmail(email)
        if (account.verified) {
            throw AccountAlreadyVerifiedException()
        }

        val event = ResendVerificationEmailEvent(
            accountId = account.id,
            email = account.email.value,
            name = account.name.value
        )
        eventPublisher.publishEvent(event)
    }
}
