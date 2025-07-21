package com.goodpon.api.dashboard.application.auth

import com.goodpon.api.dashboard.support.AbstractIntegrationTest
import com.goodpon.application.dashboard.account.port.out.AccountRepository
import com.goodpon.application.dashboard.account.service.accessor.AccountAccessor
import com.goodpon.application.dashboard.auth.port.`in`.dto.LoginCommand
import com.goodpon.application.dashboard.auth.port.out.PasswordEncoder
import com.goodpon.application.dashboard.auth.port.out.TokenProvider
import com.goodpon.application.dashboard.auth.service.LoginService
import com.goodpon.domain.account.Account
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class LoginServiceIT(
    private val tokenProvider: TokenProvider,
    private val accountAccessor: AccountAccessor,
    private val accountRepository: AccountRepository,
) : AbstractIntegrationTest() {

    class FakePasswordEncoder : PasswordEncoder {
        override fun encode(rawPassword: String): String = rawPassword
        override fun matches(rawPassword: String, encodedPassword: String): Boolean = true
    }

    val loginService = LoginService(
        passwordEncoder = FakePasswordEncoder(),
        accountAccessor = accountAccessor,
        tokenProvider = tokenProvider
    )

    @Test
    fun `로그인할 수 있다`() {
        // given
        val email = "test@goodpon.site"
        val password = "password"
        val name = "테스트 계정"

        val account = Account.create(email = email, password = password, name = name)
        accountRepository.save(account)

        val command = LoginCommand(email = email, password = password)

        // when
        val result = loginService(command)

        // then
        result.email shouldBe email
        result.accessToken.shouldNotBeNull()
    }
}
