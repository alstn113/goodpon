package com.goodpon.application.dashboard.auth.service

import com.goodpon.application.dashboard.account.service.accessor.AccountAccessor
import com.goodpon.application.dashboard.auth.port.`in`.ResendVerificationEmailUseCase
import com.goodpon.application.dashboard.auth.service.event.ResendVerificationEmailEvent
import com.goodpon.domain.account.exception.AccountAlreadyVerifiedException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ResendVerificationEmailService(
    private val accountAccessor: AccountAccessor,
    private val eventPublisher: ApplicationEventPublisher,
) : ResendVerificationEmailUseCase {

    @Transactional(readOnly = true)
    override fun invoke(email: String) {
        val account = accountAccessor.readByEmail(email)
        if (account.verified) {
            throw AccountAlreadyVerifiedException()
        }

        val event = ResendVerificationEmailEvent(
            accountId = account.id,
            email = account.email.value,
            name = account.name.value
        )
        eventPublisher.publishEvent(event)
    }
}
