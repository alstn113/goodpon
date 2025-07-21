package com.goodpon.api.dashboard.application.account

import com.goodpon.api.dashboard.support.AbstractIntegrationTest
import com.goodpon.application.dashboard.account.port.`in`.dto.SignUpCommand
import com.goodpon.application.dashboard.account.port.out.AccountRepository
import com.goodpon.application.dashboard.account.service.SignUpService
import com.goodpon.application.dashboard.auth.service.event.AccountCreatedEvent
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.verify
import org.junit.jupiter.api.Test

class SignUpServiceIT(
    private val signUpService: SignUpService,
    private val accountRepository: AccountRepository,
) : AbstractIntegrationTest() {

    @Test
    fun `회원 가입을 할 수 있다`() {
        // given
        val command = SignUpCommand(
            email = "test@goodpon.site",
            password = "password",
            name = "테스트 계정",
        )

        // when
        val result = signUpService(command)

        // then
        val savedAccount = accountRepository.findById(result.id)
        savedAccount.shouldNotBeNull()
        savedAccount.email.value shouldBe command.email

        verify {
            val event = AccountCreatedEvent(
                accountId = savedAccount.id,
                email = savedAccount.email.value,
                name = savedAccount.name.value
            )
            verificationEmailEventListener.handleAccountCreatedEvent(event)
        }
    }
}