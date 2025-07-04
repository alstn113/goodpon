package com.goodpon.core.application.account

import com.goodpon.core.application.account.response.AccountInfo
import com.goodpon.core.application.auth.VerificationEmailRequestedEvent
import com.goodpon.core.application.auth.request.SignUpRequest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService(
    private val accountRegistrationService: AccountRegistrationService,
    private val eventPublisher: ApplicationEventPublisher,
    private val accountReader: AccountReader,
) {
    @Transactional(readOnly = true)
    fun getAccountInfo(accountId: Long): AccountInfo {
        val account = accountReader.readById(accountId)

        return AccountInfo(
            id = account.id,
            email = account.email.value,
            name = account.name.value,
            verified = account.verified,
        )
    }

    @Transactional
    fun signUp(request: SignUpRequest): AccountInfo {
        val account = accountRegistrationService.register(
            email = request.email,
            name = request.name,
            password = request.password,
        )

        val event = VerificationEmailRequestedEvent(
            accountId = account.id,
            email = account.email.value,
            name = account.name.value
        )
        eventPublisher.publishEvent(event)

        return AccountInfo(
            id = account.id,
            email = account.email.value,
            name = account.name.value,
            verified = account.verified,
        )
    }
}