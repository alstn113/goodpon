package com.goodpon.dashboard.application.account.service

import com.goodpon.dashboard.application.account.port.`in`.SignUpUseCase
import com.goodpon.dashboard.application.account.port.`in`.dto.SignUpCommand
import com.goodpon.dashboard.application.account.port.`in`.dto.SignUpResult
import com.goodpon.dashboard.application.auth.service.event.AccountCreatedEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SignUpService(
    private val accountRegistrationService: AccountRegistrationService,
    private val eventPublisher: ApplicationEventPublisher,
) : SignUpUseCase {

    @Transactional
    override fun signUp(command: SignUpCommand): SignUpResult {
        val account = accountRegistrationService.register(
            email = command.email,
            name = command.name,
            password = command.password,
        )

        val event = AccountCreatedEvent(
            accountId = account.id,
            email = account.email.value,
            name = account.name.value
        )
        eventPublisher.publishEvent(event)

        return AccountMapper.toSignUpResult(account)
    }
}