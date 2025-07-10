package com.goodpon.dashboard.application.auth

import com.goodpon.dashboard.application.account.service.accessor.AccountReader
import com.goodpon.dashboard.application.auth.port.`in`.dto.LoginCommand
import com.goodpon.dashboard.application.auth.port.`in`.dto.LoginResult
import com.goodpon.dashboard.application.auth.port.out.PasswordEncoder
import com.goodpon.dashboard.application.auth.port.out.TokenProvider
import com.goodpon.dashboard.application.auth.service.LoginService
import com.goodpon.dashboard.application.auth.service.exception.PasswordMismatchException
import com.goodpon.domain.account.Account
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk

class LoginServiceTest : DescribeSpec({

    val accountReader = mockk<AccountReader>()
    val passwordEncoder = mockk<PasswordEncoder>()
    val tokenProvider = mockk<TokenProvider>()
    val loginService = LoginService(accountReader, passwordEncoder, tokenProvider)

    beforeTest {
        clearAllMocks()
    }

    describe("login") {
        val command = LoginCommand("email@goodpon.site", "hashedPassword")
        val account = Account.create(email = "email@goodpon.site", password = "password", name = "Test User")

        beforeTest {
            every { accountReader.readByEmail(command.email) } returns account
        }

        context("비밀번호가 일치하지 않을 경우") {
            beforeTest {
                every { passwordEncoder.matches(any(), any()) } returns false
            }

            it("예외를 발생시킨다.") {
                shouldThrow<PasswordMismatchException> {
                    loginService.login(command)
                }
            }
        }

        context("비밀번호가 일치하는 경우") {
            beforeTest {
                every { passwordEncoder.matches(any(), any()) } returns true
            }

            it("로그인 응답을 반환한다.") {
                val token = "access-token"
                every { tokenProvider.generateAccessToken(account.id) } returns token

                val actual = loginService.login(command)

                val expected = LoginResult(
                    id = account.id,
                    email = account.email.value,
                    name = account.name.value,
                    verified = account.verified,
                    accessToken = token
                )

                actual shouldBe expected
            }
        }
    }
})