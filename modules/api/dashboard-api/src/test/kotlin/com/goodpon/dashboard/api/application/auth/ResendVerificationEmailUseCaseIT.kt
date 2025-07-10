package com.goodpon.dashboard.api.application.auth

import com.goodpon.dashboard.api.support.AbstractIntegrationTest
import com.goodpon.dashboard.application.account.port.out.AccountRepository
import com.goodpon.dashboard.application.auth.port.`in`.ResendVerificationEmailUseCase
import com.goodpon.dashboard.application.auth.service.event.ResendVerificationEmailEvent
import com.goodpon.dashboard.application.auth.service.listener.VerificationEmailEventListener
import com.goodpon.domain.account.Account
import com.ninjasquad.springmockk.MockkBean
import io.mockk.verify
import org.junit.jupiter.api.Test

class ResendVerificationEmailUseCaseIT(
    private val resendVerificationEmailUseCase: ResendVerificationEmailUseCase,
    private val accountRepository: AccountRepository,
) : AbstractIntegrationTest() {

    @MockkBean
    private lateinit var verificationEmailEventListener: VerificationEmailEventListener

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
        resendVerificationEmailUseCase.resendVerificationEmail(email)

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