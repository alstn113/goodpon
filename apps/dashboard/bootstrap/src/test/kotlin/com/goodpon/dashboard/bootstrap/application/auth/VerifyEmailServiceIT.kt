package com.goodpon.dashboard.bootstrap.application.auth

import com.goodpon.dashboard.application.account.port.out.AccountRepository
import com.goodpon.dashboard.application.auth.port.out.EmailVerificationCache
import com.goodpon.dashboard.application.auth.port.out.dto.EmailVerificationDto
import com.goodpon.dashboard.application.auth.service.VerifyEmailService
import com.goodpon.dashboard.bootstrap.support.AbstractIntegrationTest
import com.goodpon.domain.account.Account
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class VerifyEmailServiceIT(
    private val verifyEmailService: VerifyEmailService,
    private val accountRepository: AccountRepository,
    private val emailVerificationCache: EmailVerificationCache,
) : AbstractIntegrationTest() {

    @Test
    fun `토큰으로 이메일 인증을 할 수 있다`() {
        // given
        val email = "test@goodpon.site"
        val name = "테스트 계정"
        val account = Account.create(
            email = email,
            password = "password",
            name = name
        )
        val savedAccount = accountRepository.save(account)

        val token = "valid-token"
        val emailVerificationDto = EmailVerificationDto(
            accountId = savedAccount.id,
            token = token,
            email = email,
            name = name,
            createdAt = LocalDateTime.now()
        )
        emailVerificationCache.save(emailVerificationDto)

        // when
        verifyEmailService(token)

        // then
        val verifiedAccount = accountRepository.findById(savedAccount.id)
        verifiedAccount.shouldNotBeNull()
        verifiedAccount.verified shouldBe true
        verifiedAccount.verifiedAt.shouldNotBeNull()

        val usedEmailVerification = emailVerificationCache.findByToken(token)
        usedEmailVerification.shouldBeNull()
    }
}