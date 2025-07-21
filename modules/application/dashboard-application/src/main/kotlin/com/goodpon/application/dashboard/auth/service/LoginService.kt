package com.goodpon.application.dashboard.auth.service

import com.goodpon.application.dashboard.account.service.accessor.AccountAccessor
import com.goodpon.application.dashboard.auth.port.`in`.LoginUseCase
import com.goodpon.application.dashboard.auth.port.`in`.dto.LoginCommand
import com.goodpon.application.dashboard.auth.port.`in`.dto.LoginResult
import com.goodpon.application.dashboard.auth.port.out.PasswordEncoder
import com.goodpon.application.dashboard.auth.port.out.TokenProvider
import com.goodpon.application.dashboard.auth.service.exception.PasswordMismatchException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LoginService(
    private val accountAccessor: AccountAccessor,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: TokenProvider,
) : LoginUseCase {

    @Transactional
    override fun invoke(command: LoginCommand): LoginResult {
        val account = accountAccessor.readByEmail(command.email)

        if (!passwordEncoder.matches(command.password, account.password.value)) {
            throw PasswordMismatchException()
        }

        val accessToken = tokenProvider.generateAccessToken(accountId = account.id)

        return LoginResult(
            id = account.id,
            email = account.email.value,
            name = account.name.value,
            verified = account.verified,
            accessToken = accessToken,
        )
    }
}
