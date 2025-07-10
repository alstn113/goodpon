package com.goodpon.dashboard.api.application.auth

import com.goodpon.dashboard.api.support.AbstractIntegrationTest
import com.goodpon.dashboard.application.account.port.out.AccountRepository
import com.goodpon.dashboard.application.auth.port.`in`.LoginUseCase
import com.goodpon.dashboard.application.auth.port.`in`.dto.LoginCommand
import com.goodpon.dashboard.application.auth.port.out.PasswordEncoder
import com.goodpon.domain.account.Account
import com.ninjasquad.springmockk.MockkBean
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import org.junit.jupiter.api.Test

class LoginUseCaseIT(
    private val loginUseCase: LoginUseCase,
    private val accountRepository: AccountRepository,
) : AbstractIntegrationTest() {

    @MockkBean
    private lateinit var passwordEncoder: PasswordEncoder

    @Test
    fun `로그인할 수 있다`() {
        // given
        val email = "test@goodpon.site"
        val password = "password"
        val name = "테스트 계정"

        val account = Account.create(email = email, password = password, name = name)
        accountRepository.save(account)

        every { passwordEncoder.matches(any(), any()) } returns true

        val command = LoginCommand(email = email, password = password)

        // when
        val result = loginUseCase.login(command)

        // then
        result.email shouldBe email
        result.accessToken.shouldNotBeNull()
    }
}