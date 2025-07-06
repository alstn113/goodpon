package com.goodpon.dashboard.application.account

import com.goodpon.dashboard.application.account.port.`in`.dto.AccountInfo
import com.goodpon.dashboard.application.account.port.`in`.dto.SignUpCommand
import com.goodpon.dashboard.application.account.port.`in`.dto.SignUpResult
import com.goodpon.dashboard.application.account.service.AccountRegistrationService
import com.goodpon.dashboard.application.account.service.SignUpService
import com.goodpon.dashboard.application.account.service.accessor.AccountReader
import com.goodpon.dashboard.application.auth.service.event.AccountCreatedEvent
import com.goodpon.domain.account.Account
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.context.ApplicationEventPublisher


class AccountServiceTest : DescribeSpec({

    val accountRegistrationService = mockk<AccountRegistrationService>()
    val eventPublisher = mockk<ApplicationEventPublisher>(relaxed = true)
    val accountReader = mockk<AccountReader>()
    val accountService = SignUpService(
        accountRegistrationService = accountRegistrationService,
        eventPublisher = eventPublisher,
        accountReader = accountReader
    )

    beforeEach { clearAllMocks() }

    describe("getAccountInfo") {
        it("계정 식별자로 계정 정보를 조회할 수 있다.") {
            val accountId = 1L
            val account = Account.create(
                email = "test@email.com",
                password = "password",
                name = "테스터"
            ).copy(id = accountId, verified = true)
            every { accountReader.readById(accountId) } returns account

            val result = accountService.getAccountInfo(accountId)

            result shouldBe AccountInfo(
                id = account.id,
                email = account.email.value,
                name = account.name.value,
                verified = account.verified
            )
            verify { accountReader.readById(accountId) }
        }
    }

    describe("signUp") {
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

            val result = accountService.signUp(command)

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
    }
})