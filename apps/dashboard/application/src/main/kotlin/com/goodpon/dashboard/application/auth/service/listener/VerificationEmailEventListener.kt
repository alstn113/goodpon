package com.goodpon.dashboard.application.auth.service.listener

import com.goodpon.dashboard.application.auth.service.SendVerificationEmailService
import com.goodpon.dashboard.application.auth.service.event.AccountCreatedEvent
import com.goodpon.dashboard.application.auth.service.event.ResendVerificationEmailEvent
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class VerificationEmailEventListener(
    private val sendVerificationEmailService: SendVerificationEmailService,
) {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleAccountCreatedEvent(event: AccountCreatedEvent) {
        sendVerificationEmailService.send(
            event.accountId,
            event.email,
            event.name
        )
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleResendVerificationEmailEvent(event: ResendVerificationEmailEvent) {
        sendVerificationEmailService.send(
            event.accountId,
            event.email,
            event.name
        )
    }
}