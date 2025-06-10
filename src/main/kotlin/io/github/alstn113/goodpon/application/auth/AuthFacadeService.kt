package io.github.alstn113.goodpon.application.auth

import io.github.alstn113.goodpon.application.auth.request.RegisterRequest
import io.github.alstn113.goodpon.application.auth.request.ResendVerificationEmailRequest
import io.github.alstn113.goodpon.application.auth.request.VerifyEmailRequest
import io.github.alstn113.goodpon.domain.auth.AccountRegisteredEvent
import io.github.alstn113.goodpon.domain.auth.AccountRegistrationService
import io.github.alstn113.goodpon.domain.auth.EmailVerificationService
import io.github.alstn113.goodpon.domain.auth.VerificationEmailResendRequestedEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class AuthFacadeService(
    private val accountRegistrationService: AccountRegistrationService,
    private val emailVerificationService: EmailVerificationService,
    private val eventPublisher: ApplicationEventPublisher,
) {

    @Transactional
    fun registerAccount(request: RegisterRequest): Long {
        val savedAccount = accountRegistrationService.registerAccount(
            request.email,
            request.password,
            request.name
        )

        val event = AccountRegisteredEvent(savedAccount.id, savedAccount.email, savedAccount.name)
        eventPublisher.publishEvent(event)

        return savedAccount.id
    }

    @Transactional(readOnly = true)
    fun resendVerificationEmail(request: ResendVerificationEmailRequest) {
        val account = emailVerificationService.validateResendRequest(request.email)

        val event = VerificationEmailResendRequestedEvent(account.id, account.email, account.name)
        eventPublisher.publishEvent(event)
    }

    @Transactional
    fun verifyEmail(request: VerifyEmailRequest) {
        emailVerificationService.verifyAccountEmail(request.token)
    }
}