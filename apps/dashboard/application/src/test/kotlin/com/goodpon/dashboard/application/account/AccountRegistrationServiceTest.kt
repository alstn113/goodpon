package com.goodpon.dashboard.application.account

import com.goodpon.dashboard.application.account.service.AccountRegistrationService
import com.goodpon.dashboard.application.account.service.accessor.AccountAccessor
import com.goodpon.dashboard.application.account.service.exception.AccountEmailExistsException
import com.goodpon.dashboard.application.auth.port.out.PasswordEncoder
import com.goodpon.domain.account.Account
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class AccountRegistrationServiceTest : DescribeSpec({

    val accountAccessor = mockk<AccountAccessor>()
    val passwordEncoder = mockk<PasswordEncoder>()
    val accountRegistrationService = AccountRegistrationService(accountAccessor, passwordEncoder)

    describe("register") {
        val email = "test@goodpon.site"
        val password = "password123"
        val name = "testUser"
        val account = Account.create(email, password, name)

        it("계정을 등록할 수 있다.") {
            val hashedPassword = "hashedPassword123"

            every { accountAccessor.existsByEmail(email) } returns false
            every { passwordEncoder.encode(password) } returns hashedPassword
            every { accountAccessor.create(any()) } returns account

            val result = accountRegistrationService.register(email, password, name)

            result shouldBe account
        }

        it("이미 존재하는 계정 이메일일 경우 계정을 등록할 수 없다.") {
            every { accountAccessor.existsByEmail(email) } returns true

            shouldThrow<AccountEmailExistsException> {
                accountRegistrationService.register(email, password, name)
            }
        }
    }
})