package io.github.alstn113.goodpon.application.auth

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class AccountEventListener(
    private val emailVerificationService: EmailVerificationService,
) {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleAccountRegisteredEvent(event: AccountRegisteredEvent) {
        emailVerificationService.sendVerificationEmail(
            accountId = event.accountId,
            email = event.email,
            name = event.name
        )
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleVerificationEmailResentEvent(event: VerificationEmailResentEvent) {
        emailVerificationService.sendVerificationEmail(
            accountId = event.accountId,
            email = event.email,
            name = event.name
        )
    }
}