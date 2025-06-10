package com.goodpon.application.auth

import com.goodpon.goodpon.domain.auth.AccountRegisteredEvent
import com.goodpon.goodpon.domain.auth.VerificationEmailResendRequestedEvent
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class AccountEventListener(
    private val verificationEmailService: VerificationEmailService,
) {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleAccountRegistered(event: AccountRegisteredEvent) {
        verificationEmailService.sendVerificationEmail(
            event.accountId,
            event.email,
            event.name
        )
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleVerificationEmailResendRequested(event: VerificationEmailResendRequestedEvent) {
        verificationEmailService.sendVerificationEmail(
            event.accountId,
            event.email,
            event.name
        )
    }
}