package com.goodpon.common.application.auth

import com.goodpon.common.application.auth.request.EmailVerifyRequest
import com.goodpon.common.application.auth.request.RegisterRequest
import com.goodpon.common.application.auth.request.VerificationEmailResendRequest
import com.goodpon.common.domain.auth.AccountRegisteredEvent
import com.goodpon.common.domain.auth.AccountRegistrationService
import com.goodpon.common.domain.auth.EmailVerificationService
import com.goodpon.common.domain.auth.VerificationEmailResendRequestedEvent
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
    fun register(request: RegisterRequest): Long {
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
    fun resendVerificationEmail(request: VerificationEmailResendRequest) {
        val account = emailVerificationService.validateResendRequest(request.email)

        val event = VerificationEmailResendRequestedEvent(account.id, account.email, account.name)
        eventPublisher.publishEvent(event)
    }

    @Transactional
    fun verifyEmail(request: EmailVerifyRequest) {
        emailVerificationService.verifyAccountEmail(request.token)
    }
}