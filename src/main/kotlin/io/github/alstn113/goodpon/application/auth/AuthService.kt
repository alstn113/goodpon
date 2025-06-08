package io.github.alstn113.goodpon.application.auth

import io.github.alstn113.goodpon.application.auth.request.RegisterRequest
import io.github.alstn113.goodpon.application.auth.request.ResendVerificationEmailRequest
import io.github.alstn113.goodpon.application.auth.request.VerifyEmailRequest
import io.github.alstn113.goodpon.domain.account.Account
import io.github.alstn113.goodpon.domain.account.AccountAppender
import io.github.alstn113.goodpon.domain.account.AccountReader
import io.github.alstn113.goodpon.domain.account.PasswordEncoder
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val accountReader: AccountReader,
    private val accountAppender: AccountAppender,
    private val passwordEncoder: PasswordEncoder,
    private val eventPublisher: ApplicationEventPublisher,
) {

    @Transactional
    fun register(request: RegisterRequest): Long {
        if (accountReader.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email already exists")
        }

        val hashedPassword = passwordEncoder.encode(request.password)
        val account = Account.create(
            email = request.email,
            password = hashedPassword,
            name = request.name,
        )
        val savedAccount = accountAppender.append(account)

        eventPublisher.publishEvent(
            AccountRegisteredEvent(
                accountId = savedAccount.id,
                email = savedAccount.email,
                name = savedAccount.name
            )
        )

        return account.id
    }

    @Transactional
    fun verify(request: VerifyEmailRequest) {

        // redis 에서 인증 토큰 추출
        // 있으면 넘어가고,
        // 계정이 활성화 상태면 활성화 처리
    }

    @Transactional(readOnly = true)
    fun resendVerificationEmail(request: ResendVerificationEmailRequest) {
        val account = accountReader.readByEmail(request.email)

        if (account.isActive()) {
            throw IllegalStateException("Account is already active")
        }

        eventPublisher.publishEvent(
            VerificationEmailResentEvent(
                accountId = account.id,
                email = account.email,
                name = account.name,
            )
        )
    }
}