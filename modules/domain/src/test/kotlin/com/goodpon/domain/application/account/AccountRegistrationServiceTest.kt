package com.goodpon.domain.application.account

import com.goodpon.domain.application.account.accessor.AccountReader
import com.goodpon.domain.application.account.accessor.AccountStore
import com.goodpon.domain.application.account.exception.AccountEmailExistsException
import com.goodpon.domain.domain.account.Account
import com.goodpon.domain.application.auth.PasswordEncoder
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class AccountRegistrationServiceTest : DescribeSpec({

    val accountReader = mockk<AccountReader>()
    val accountStore = mockk<AccountStore>()
    val passwordEncoder = mockk<PasswordEncoder>()
    val accountRegistrationService = AccountRegistrationService(accountReader, accountStore, passwordEncoder)

    describe("register") {
        val email = "test@goodpon.site"
        val password = "password123"
        val name = "testUser"
        val account = Account.create(email, password, name)

        it("계정을 등록할 수 있다.") {
            val hashedPassword = "hashedPassword123"

            every { accountReader.existsByEmail(email) } returns false
            every { passwordEncoder.encode(password) } returns hashedPassword
            every { accountStore.create(any()) } returns account

            val result = accountRegistrationService.register(email, password, name)

            result shouldBe account
        }

        it("이미 존재하는 계정 이메일일 경우 계정을 등록할 수 없다.") {
            every { accountReader.existsByEmail(email) } returns true

            shouldThrow<AccountEmailExistsException> {
                accountRegistrationService.register(email, password, name)
            }
        }
    }
})