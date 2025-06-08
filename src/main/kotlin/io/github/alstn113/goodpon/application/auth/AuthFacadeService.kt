package io.github.alstn113.goodpon.application.auth

import io.github.alstn113.goodpon.application.auth.event.AccountRegisteredEvent
import io.github.alstn113.goodpon.application.auth.event.VerificationEmailResendRequestedEvent
import io.github.alstn113.goodpon.application.auth.request.RegisterRequest
import io.github.alstn113.goodpon.application.auth.request.ResendVerificationEmailRequest
import io.github.alstn113.goodpon.application.auth.request.VerifyEmailRequest
import io.github.alstn113.goodpon.domain.auth.AccountRegistrationService
import io.github.alstn113.goodpon.domain.auth.EmailVerificationService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service


@Service
class AuthFacadeService(
    private val accountRegistrationService: AccountRegistrationService,
    private val emailVerificationService: EmailVerificationService,
    private val eventPublisher: ApplicationEventPublisher,
) {

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

    fun resendVerificationEmail(request: ResendVerificationEmailRequest) {
        val account = emailVerificationService.validateResendRequest(request.email)

        val event = VerificationEmailResendRequestedEvent(account.id, account.email, account.name)
        eventPublisher.publishEvent(event)
    }

    fun verifyEmail(request: VerifyEmailRequest) {
        emailVerificationService.verifyAccountEmail(request.token)
    }
}