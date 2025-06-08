package io.github.alstn113.goodpon.application.auth

import io.github.alstn113.goodpon.application.auth.event.AccountRegisteredEvent
import io.github.alstn113.goodpon.application.auth.event.VerificationEmailResendRequestedEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class AccountEventListener(
    private val verificationEmailService: VerificationEmailService,
) {

    @Async
    @EventListener
    fun handleAccountRegistered(event: AccountRegisteredEvent) {
        verificationEmailService.sendVerificationEmail(
            event.accountId,
            event.email,
            event.name
        )
    }

    @Async
    @EventListener
    fun handleVerificationEmailResendRequested(event: VerificationEmailResendRequestedEvent) {
        verificationEmailService.sendVerificationEmail(
            event.accountId,
            event.email,
            event.name
        )
    }
}