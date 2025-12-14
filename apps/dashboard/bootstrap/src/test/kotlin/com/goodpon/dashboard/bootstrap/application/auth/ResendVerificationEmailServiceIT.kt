package com.goodpon.dashboard.bootstrap.application.auth

import com.goodpon.dashboard.application.account.port.out.AccountRepository
import com.goodpon.dashboard.application.auth.service.ResendVerificationEmailService
import com.goodpon.dashboard.application.auth.service.event.ResendVerificationEmailEvent
import com.goodpon.dashboard.bootstrap.support.AbstractIntegrationTest
import com.goodpon.domain.account.Account
import io.mockk.verify
import org.junit.jupiter.api.Test

class ResendVerificationEmailServiceIT(
    private val resendVerificationEmailService: ResendVerificationEmailService,
    private val accountRepository: AccountRepository,
) : AbstractIntegrationTest() {

    @Test
    fun `인증 이메일에 대한 재전송을 요청할 수 있다`() {
        // given
        val email = "test@goodpon.site"
        val account = Account.create(
            email = email,
            password = "password",
            name = "테스트 계정"
        )
        val savedAccount = accountRepository.save(account)

        // when
        resendVerificationEmailService(email)

        // then
        verify {
            val event = ResendVerificationEmailEvent(
                accountId = savedAccount.id,
                email = savedAccount.email.value,
                name = savedAccount.name.value
            )
            verificationEmailEventListener.handleResendVerificationEmailEvent(event)
        }
    }
}