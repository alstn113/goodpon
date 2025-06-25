package com.goodpon.core.application.auth

import com.goodpon.core.application.auth.request.SignUpRequest
import com.goodpon.core.application.auth.request.ResendVerificationEmailRequest
import com.goodpon.core.application.auth.request.VerifyEmailRequest
import com.goodpon.core.domain.auth.AccountRegisteredEvent
import com.goodpon.core.domain.auth.AccountRegistrationService
import com.goodpon.core.domain.auth.EmailVerificationService
import com.goodpon.core.domain.auth.VerificationEmailResendRequestedEvent
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
    fun register(request: SignUpRequest): Long {
        val savedAccount = accountRegistrationService.register(
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
        emailVerificationService.verifyAccountEmail(request.code)
    }
}