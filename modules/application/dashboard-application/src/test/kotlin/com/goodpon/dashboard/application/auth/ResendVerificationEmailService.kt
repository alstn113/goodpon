package com.goodpon.dashboard.application.auth

import com.goodpon.dashboard.application.account.service.accessor.AccountReader
import com.goodpon.dashboard.application.auth.service.ResendVerificationEmailService
import com.goodpon.dashboard.application.auth.service.event.ResendVerificationEmailEvent
import com.goodpon.domain.account.Account
import com.goodpon.domain.account.exception.AccountAlreadyVerifiedException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.context.ApplicationEventPublisher

class ResendVerificationEmailService : DescribeSpec({

    val accountReader = mockk<AccountReader>()
    val eventPublisher = mockk<ApplicationEventPublisher>(relaxed = true)
    val resendVerificationEmailService = ResendVerificationEmailService(accountReader, eventPublisher)

    beforeTest {
        clearAllMocks()
    }

    describe("resendVerificationEmail") {
        val account = Account.create(
            email = "email@goodpon.site",
            password = "password",
            name = "Test User",
        )

        context("계정이 이미 인증된 경우") {
            val verifiedAccount = account.copy(verified = true)
            beforeTest {
                every { accountReader.readByEmail(verifiedAccount.email.value) } returns verifiedAccount
            }

            it("예외를 발생시킨다.") {
                shouldThrow<AccountAlreadyVerifiedException> {
                    resendVerificationEmailService.resendVerificationEmail(verifiedAccount.email.value)
                }
            }
        }
        context("계정이 아직 인증되지 않은 경우") {
            beforeTest {
                every { accountReader.readByEmail(account.email.value) } returns account.copy(verified = false)
            }

            it("이메일 인증 재전송 이벤트를 발행한다.") {
                resendVerificationEmailService.resendVerificationEmail(account.email.value)

                verify {
                    eventPublisher.publishEvent(
                        ResendVerificationEmailEvent(
                            accountId = account.id,
                            email = account.email.value,
                            name = account.name.value
                        )
                    )
                }
            }
        }
    }
})