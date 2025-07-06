package com.goodpon.partner.application.auth.listener

import com.goodpon.partner.application.auth.EmailVerificationSender
import com.goodpon.partner.application.auth.event.AccountCreatedEvent
import com.goodpon.partner.application.auth.event.ResendVerificationEmailEvent
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
    fun handleAccountCreatedEvent(event: AccountCreatedEvent) {
        emailVerificationSender.send(
            event.accountId,
            event.email,
            event.name
        )
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleResendVerificationEmailEvent(event: ResendVerificationEmailEvent) {
        emailVerificationSender.send(
            event.accountId,
            event.email,
            event.name
        )
    }
}