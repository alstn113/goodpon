package com.goodpon.dashboard.application.auth.listener

import com.goodpon.dashboard.application.auth.service.SendVerificationEmailService
import com.goodpon.dashboard.application.auth.service.event.AccountCreatedEvent
import com.goodpon.dashboard.application.auth.service.event.ResendVerificationEmailEvent
import com.goodpon.dashboard.application.auth.service.listener.VerificationEmailEventListener
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.mockk
import io.mockk.verify

class VerificationEmailEventListenerTest : DescribeSpec({

    val sendVerificationEmailService = mockk<SendVerificationEmailService>(relaxed = true)
    val verificationEmailEventListener = VerificationEmailEventListener(sendVerificationEmailService)

    describe("AccountCreatedEvent 수신 시") {
        val event = AccountCreatedEvent(1L, "test@example.com", "홍길동")

        it("이메일 인증 메일을 발송한다") {
            verificationEmailEventListener.handleAccountCreatedEvent(event)

            verify {
                sendVerificationEmailService.send(
                    event.accountId,
                    event.email,
                    event.name
                )
            }
        }
    }

    describe("ResendVerificationEmailEvent 수신 시") {
        val event = ResendVerificationEmailEvent(2L, "test@example.com", "홍길동")

        it("이메일 인증 메일을 재발송한다") {
            verificationEmailEventListener.handleResendVerificationEmailEvent(event)

            verify {
                sendVerificationEmailService.send(
                    event.accountId,
                    event.email,
                    event.name
                )
            }
        }
    }
})