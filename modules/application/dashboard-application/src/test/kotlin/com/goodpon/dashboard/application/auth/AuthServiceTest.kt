package com.goodpon.dashboard.application.auth

import com.goodpon.dashboard.application.account.AccountVerificationService
import com.goodpon.dashboard.application.account.accessor.AccountReader
import com.goodpon.dashboard.application.auth.accessor.EmailVerificationReader
import com.goodpon.dashboard.application.auth.accessor.EmailVerificationStore
import com.goodpon.dashboard.application.auth.event.ResendVerificationEmailEvent
import com.goodpon.dashboard.application.auth.exception.PasswordMismatchException
import com.goodpon.dashboard.application.auth.request.LoginRequest
import com.goodpon.dashboard.application.auth.response.LoginResponse
import com.goodpon.domain.account.Account
import com.goodpon.domain.account.exception.AccountAlreadyVerifiedException
import com.goodpon.domain.auth.EmailVerification
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.context.ApplicationEventPublisher
import java.time.LocalDateTime

class AuthServiceTest : DescribeSpec({

    val accountReader = mockk<AccountReader>()
    val passwordEncoder = mockk<PasswordEncoder>()
    val tokenProvider = mockk<TokenProvider>()
    val accountVerificationService = mockk<AccountVerificationService>()
    val emailVerificationStore = mockk<EmailVerificationStore>()
    val emailVerificationReader = mockk<EmailVerificationReader>()
    val eventPublisher = mockk<ApplicationEventPublisher>(relaxed = true)
    val authService = AuthService(
        accountReader = accountReader,
        passwordEncoder = passwordEncoder,
        tokenProvider = tokenProvider,
        accountVerificationService = accountVerificationService,
        emailVerificationStore = emailVerificationStore,
        emailVerificationReader = emailVerificationReader,
        eventPublisher = eventPublisher
    )

    val account = Account.create(
        email = "email@goodpon.site",
        password = "password",
        name = "Test User",
    )

    beforeTest {
        clearAllMocks()
    }

    describe("login") {
        val request = LoginRequest("email@goodpon.site", "hashedPassword")

        beforeTest {
            every { accountReader.readByEmail(request.email) } returns account
        }

        context("비밀번호가 일치하지 않을 경우") {
            beforeTest {
                every { passwordEncoder.matches(any(), any()) } returns false
            }

            it("예외를 발생시킨다.") {
                shouldThrow<PasswordMismatchException> {
                    authService.login(request)
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

                val actual = authService.login(request)

                val expected = LoginResponse(
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

    describe("verifyEmail") {
        val token = "verify-token"
        val verification = EmailVerification(
            accountId = account.id,
            token = token,
            email = account.email.value,
            name = account.name.value,
            createdAt = LocalDateTime.now()
        )

        it("이메일 인증 처리를 정상적으로 수행한다") {
            every { emailVerificationReader.readByToken(token) } returns verification
            every { accountVerificationService.verifyEmail(account.id, any()) } returns mockk<Account>()
            every { emailVerificationStore.delete(token, account.id) } returns Unit

            authService.verifyEmail(token)

            verify { emailVerificationReader.readByToken(token) }
            verify { accountVerificationService.verifyEmail(accountId = account.id, verifiedAt = any()) }
            verify { emailVerificationStore.delete(token = token, accountId = account.id) }
        }
    }

    describe("resendVerificationEmail") {
        context("계정이 이미 인증된 경우") {
            val verifiedAccount = account.copy(verified = true)
            beforeTest {
                every { accountReader.readByEmail(verifiedAccount.email.value) } returns verifiedAccount
            }

            it("예외를 발생시킨다.") {
                shouldThrow<AccountAlreadyVerifiedException> {
                    authService.resendVerificationEmail(verifiedAccount.email.value)
                }
            }
        }
        context("계정이 아직 인증되지 않은 경우") {
            beforeTest {
                every { accountReader.readByEmail(account.email.value) } returns account.copy(verified = false)
            }

            it("이메일 인증 재전송 이벤트를 발행한다.") {
                authService.resendVerificationEmail(account.email.value)

                verify {
                    eventPublisher.publishEvent(
                        ResendVerificationEmailEvent(
                            accountId = account.id,
                            email = account.email.value,
                            name = account.name.value
                        )
                    )
                }
            }
        }
    }
})