package com.goodpon.domain.application.account

import com.goodpon.domain.application.account.accessor.AccountReader
import com.goodpon.domain.application.account.accessor.AccountStore
import com.goodpon.domain.domain.account.Account
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDateTime

class AccountVerificationServiceTest : DescribeSpec({

    val accountReader = mockk<AccountReader>()
    val accountStore = mockk<AccountStore>()
    val accountVerificationService = AccountVerificationService(accountReader, accountStore)

    beforeEach { clearAllMocks() }

    describe("verifyEmail") {
        it("이메일을 인증 완료 할 수 있다.") {
            val verifiedAt = LocalDateTime.of(2025, 7, 5, 12, 0)
            val account = Account.create(
                email = "email@goodpon.site",
                password = "password",
                name = "name"
            )
            val verifiedAccount = account.copy(
                verified = true,
                verifiedAt = verifiedAt
            )

            every { accountReader.readById(any()) } returns account
            every { accountStore.update(any()) } returns verifiedAccount

            val result = accountVerificationService.verifyEmail(1L, verifiedAt)

            result.verified shouldBe true
            result.verifiedAt shouldBe verifiedAt

            verify {
                accountReader.readById(1L)
                accountStore.update(
                    match { it.id == account.id && it.verified && it.verifiedAt == verifiedAt }
                )
            }
        }
    }
})