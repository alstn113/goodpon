package com.goodpon.application.dashboard.account

import com.goodpon.application.dashboard.account.port.`in`.dto.SignUpCommand
import com.goodpon.application.dashboard.account.port.`in`.dto.SignUpResult
import com.goodpon.application.dashboard.account.service.AccountRegistrationService
import com.goodpon.application.dashboard.account.service.SignUpService
import com.goodpon.application.dashboard.auth.service.event.AccountCreatedEvent
import com.goodpon.domain.account.Account
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.context.ApplicationEventPublisher

class SignUpServiceTest : DescribeSpec({

    val accountRegistrationService = mockk<AccountRegistrationService>()
    val eventPublisher = mockk<ApplicationEventPublisher>(relaxed = true)
    val signUpService = SignUpService(accountRegistrationService, eventPublisher)

    it("회원가입 시 계정을 생성하고 이벤트를 발행하며 AccountInfo를 반환한다") {
        val command = SignUpCommand(
            email = "email@goodpon.site",
            password = "password",
            name = "name"
        )
        val account = Account.create(
            email = command.email,
            password = command.password,
            name = command.name
        ).copy(id = 1L, verified = false)

        every {
            accountRegistrationService.register(
                command.email,
                command.password,
                command.name
            )
        } returns account

        val result = signUpService(command)

        result shouldBe SignUpResult(
            id = account.id,
            email = account.email.value,
            name = account.name.value,
            verified = account.verified
        )

        verify {
            accountRegistrationService.register(
                command.email,
                command.password,
                command.name
            )
        }

        verify {
            eventPublisher.publishEvent(
                AccountCreatedEvent(
                    accountId = account.id,
                    email = account.email.value,
                    name = account.name.value
                )
            )
        }
    }
})