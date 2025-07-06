package com.goodpon.dashboard.application.auth.accessor

import com.goodpon.domain.auth.EmailVerificationRepository
import com.goodpon.domain.auth.exception.EmailVerificationNotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk

class EmailVerificationReaderTest : DescribeSpec({
    val emailVerificationRepository = mockk<EmailVerificationRepository>()
    val emailVerificationReader = EmailVerificationReader(emailVerificationRepository)

    describe("readByToken") {
        it("존재하지 않는 경우 예외를 발생시킨다.") {
            every {
                emailVerificationRepository.findByToken(any())
            } returns null

            shouldThrow<EmailVerificationNotFoundException> {
                emailVerificationReader.readByToken("notExistsToken")
            }
        }
    }
})