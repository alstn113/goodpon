package com.goodpon.core.domain.account.service

import com.goodpon.core.domain.account.Account
import com.goodpon.core.domain.account.AccountRepository
import com.goodpon.core.domain.account.PasswordEncoder
import com.goodpon.core.support.error.CoreException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class AccountRegistrationServiceTest : DescribeSpec({
    val accountRepository = mockk<AccountRepository>()
    val passwordEncoder = mockk<PasswordEncoder>()
    val accountRegistrationService = AccountRegistrationService(accountRepository, passwordEncoder)

    describe("register") {
        val email = "test@goodpon.site"
        val password = "password123"
        val name = "testUser"
        val account = Account.create(email, password, name)

        it("계정을 등록할 수 있다.") {
            val hashedPassword = "hashedPassword123"

            every { accountRepository.existsByEmail(email) } returns false
            every { passwordEncoder.encode(password) } returns hashedPassword
            every { accountRepository.save(any()) } returns account

            val result = accountRegistrationService.register(email, password, name)

            result shouldBe account
        }

        it("이미 존재하는 계정 이메일일 경우 계정을 등록할 수 없다.") {
            every { accountRepository.existsByEmail(email) } returns true

            val exception = shouldThrow<CoreException> {
                accountRegistrationService.register(email, password, name)
            }
            exception.errorType.message shouldBe "이미 존재하는 계정 이메일입니다."
            exception.errorType.statusCode shouldBe 400
        }
    }
})