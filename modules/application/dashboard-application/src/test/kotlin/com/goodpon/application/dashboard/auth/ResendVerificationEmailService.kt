package com.goodpon.application.dashboard.auth

import com.goodpon.application.dashboard.account.service.accessor.AccountAccessor
import com.goodpon.application.dashboard.auth.service.ResendVerificationEmailService
import com.goodpon.application.dashboard.auth.service.event.ResendVerificationEmailEvent
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

    val accountAccessor = mockk<AccountAccessor>()
    val eventPublisher = mockk<ApplicationEventPublisher>(relaxed = true)
    val resendVerificationEmailService = ResendVerificationEmailService(accountAccessor, eventPublisher)

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
                every { accountAccessor.readByEmail(verifiedAccount.email.value) } returns verifiedAccount
            }

            it("예외를 발생시킨다.") {
                shouldThrow<AccountAlreadyVerifiedException> {
                    resendVerificationEmailService(verifiedAccount.email.value)
                }
            }
        }
        context("계정이 아직 인증되지 않은 경우") {
            beforeTest {
                every { accountAccessor.readByEmail(account.email.value) } returns account.copy(verified = false)
            }

            it("이메일 인증 재전송 이벤트를 발행한다.") {
                resendVerificationEmailService(account.email.value)

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