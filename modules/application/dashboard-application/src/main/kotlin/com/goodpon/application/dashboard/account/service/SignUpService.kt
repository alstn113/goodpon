package com.goodpon.application.dashboard.account.service

import com.goodpon.application.dashboard.account.port.`in`.SignUpUseCase
import com.goodpon.application.dashboard.account.port.`in`.dto.SignUpCommand
import com.goodpon.application.dashboard.account.port.`in`.dto.SignUpResult
import com.goodpon.application.dashboard.auth.service.event.AccountCreatedEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SignUpService(
    private val accountRegistrationService: AccountRegistrationService,
    private val eventPublisher: ApplicationEventPublisher,
) : SignUpUseCase {

    @Transactional
    override fun invoke(command: SignUpCommand): SignUpResult {
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