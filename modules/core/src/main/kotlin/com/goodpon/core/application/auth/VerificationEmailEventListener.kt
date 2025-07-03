package com.goodpon.core.application.auth

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class VerificationEmailEventListener(
    private val emailVerificationSender: EmailVerificationSender,
) {
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleVerificationEmailRequestedEvent(event: VerificationEmailRequestedEvent) {
        emailVerificationSender.send(
            event.accountId,
            event.email,
            event.name
        )
    }
}