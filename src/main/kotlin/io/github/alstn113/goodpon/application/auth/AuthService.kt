package io.github.alstn113.goodpon.application.auth

import io.github.alstn113.goodpon.application.auth.request.RegisterRequest
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
    fun verify() {

    }
}