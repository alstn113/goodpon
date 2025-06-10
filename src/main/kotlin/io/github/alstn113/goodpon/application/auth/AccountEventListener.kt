package io.github.alstn113.goodpon.application.auth

import io.github.alstn113.goodpon.domain.auth.AccountRegisteredEvent
import io.github.alstn113.goodpon.domain.auth.VerificationEmailResendRequestedEvent
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