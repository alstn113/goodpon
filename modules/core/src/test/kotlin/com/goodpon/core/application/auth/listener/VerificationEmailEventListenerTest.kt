package com.goodpon.core.application.auth.listener

import com.goodpon.core.application.auth.EmailVerificationSender
import com.goodpon.core.application.auth.event.AccountCreatedEvent
import com.goodpon.core.application.auth.event.ResendVerificationEmailEvent
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.mockk
import io.mockk.verify

class VerificationEmailEventListenerTest : DescribeSpec({

    val emailVerificationSender = mockk<EmailVerificationSender>(relaxed = true)
    val verificationEmailEventListener = VerificationEmailEventListener(emailVerificationSender)

    describe("AccountCreatedEvent 수신 시") {
        val event = AccountCreatedEvent(1L, "test@example.com", "홍길동")

        it("이메일 인증 메일을 발송한다") {
            verificationEmailEventListener.handleAccountCreatedEvent(event)

            verify {
                emailVerificationSender.send(
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
                emailVerificationSender.send(
                    event.accountId,
                    event.email,
                    event.name
                )
            }
        }
    }
})