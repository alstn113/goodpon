package com.goodpon.dashboard.api.e2e

import com.goodpon.dashboard.api.controller.v1.account.dto.SignUpRequest
import com.goodpon.dashboard.api.controller.v1.auth.dto.LoginRequest
import com.goodpon.dashboard.api.controller.v1.auth.dto.ResendVerificationEmailRequest
import com.goodpon.dashboard.api.controller.v1.auth.dto.VerifyEmailRequest
import com.goodpon.dashboard.api.support.AbstractEndToEndTest
import com.goodpon.dashboard.application.account.port.`in`.dto.AccountInfo
import com.goodpon.dashboard.application.account.port.`in`.dto.SignUpResult
import com.goodpon.dashboard.application.auth.port.`in`.dto.LoginResult
import com.goodpon.dashboard.application.auth.service.VerificationTokenGenerator
import com.ninjasquad.springmockk.SpykBean
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.jupiter.api.Test

class AccountAuthE2eTest : AbstractEndToEndTest() {

    @SpykBean
    private lateinit var verificationTokenGenerator: VerificationTokenGenerator

    private val email = "test@goodpon.site"
    private val password = "test-password"
    private val name = "테스트 계정"
    private val emailToken = "test-verification-token"
    private var accessToken = ""

    @Test
    fun `계정 인증 시나리오`() {
        회원가입()
        로그인()
        인증_이메일_재전송()
        이메일_인증()
        내_정보_조회()
    }

    private fun 회원가입() {
        // given
        val request = SignUpRequest(
            email = email,
            password = password,
            name = name
        )

        every { verificationTokenGenerator.generate() } returns emailToken

        // when
        val response = given()
            .contentType(ContentType.JSON)
            .body(request)
            .`when`()
            .post("/v1/account/sign-up")

        // then
        response.statusCode() shouldBe 200
        val responseData = response.toApiResponse<SignUpResult>()
        responseData.email shouldBe email
        responseData.verified shouldBe false
    }

    private fun 로그인() {
        // given
        val request = LoginRequest(
            email = email,
            password = password
        )

        // when
        val response = given()
            .contentType(ContentType.JSON)
            .body(request)
            .`when`()
            .post("/v1/auth/login")

        // then
        response.statusCode() shouldBe 200
        val responseData = response.toApiResponse<LoginResult>()
        responseData.verified shouldBe false

        accessToken = responseData.accessToken
    }

    private fun 인증_이메일_재전송() {
        // given
        val request = ResendVerificationEmailRequest(email = email)

        every { verificationTokenGenerator.generate() } returns emailToken

        // when
        val response = given()
            .contentType(ContentType.JSON)
            .body(request)
            .`when`()
            .post("/v1/auth/verify/resend")

        // then
        response.statusCode() shouldBe 200
    }

    private fun 이메일_인증() {
        // given
        val request = VerifyEmailRequest(token = emailToken)

        // when
        val response = given()
            .withAuthHeader(accessToken)
            .contentType(ContentType.JSON)
            .body(request)
            .`when`()
            .post("/v1/auth/verify")

        // then
        response.statusCode() shouldBe 200
    }

    private fun 내_정보_조회() {
        // when
        val response = given()
            .contentType(ContentType.JSON)
            .withAuthHeader(accessToken)
            .`when`()
            .get("/v1/account")

        // then
        response.statusCode() shouldBe 200
        val responseData = response.toApiResponse<AccountInfo>()
        responseData.email shouldBe email
        responseData.verified shouldBe true
    }
}