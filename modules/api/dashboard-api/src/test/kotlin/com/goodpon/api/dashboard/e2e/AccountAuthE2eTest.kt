package com.goodpon.api.dashboard.e2e

import com.goodpon.api.dashboard.controller.v1.account.dto.SignUpRequest
import com.goodpon.api.dashboard.controller.v1.auth.dto.LoginRequest
import com.goodpon.api.dashboard.controller.v1.auth.dto.ResendVerificationEmailRequest
import com.goodpon.api.dashboard.controller.v1.auth.dto.VerifyEmailRequest
import com.goodpon.api.dashboard.support.AbstractEndToEndTest
import com.goodpon.api.dashboard.support.FakeVerificationTokenGenerator
import com.goodpon.application.dashboard.account.port.`in`.dto.SignUpResult
import com.goodpon.application.dashboard.auth.port.`in`.dto.LoginResult
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.response.Response
import org.junit.jupiter.api.Test

class AccountAuthE2eTest : AbstractEndToEndTest() {

    @Test
    fun `계정 인증 시나리오`() {
        val email = "test@goodpon.site"
        val password = "test-password"
        val name = "테스트 계정"

        val (response, emailToken) = 회원가입_요청(email = email, password = password, name = name)
        response.apply { statusCode() shouldBe 200 }
            .toApiResponse<SignUpResult>()
            .apply { verified shouldBe false }

        val accessToken = 로그인_요청(email = email, password = password)
            .apply { statusCode() shouldBe 200 }
            .toApiResponse<LoginResult>()
            .accessToken

        내_정보_조회_요청(accessToken = accessToken)
            .apply { statusCode() shouldBe 200 }
            .toApiResponse<SignUpResult>()
            .apply { verified shouldBe false }

        이메일_인증_요청(emailToken = emailToken)
            .apply { statusCode() shouldBe 200 }

        내_정보_조회_요청(accessToken = accessToken)
            .apply { statusCode() shouldBe 200 }
            .toApiResponse<SignUpResult>()
            .apply { verified shouldBe true }
    }

    @Test
    fun `인증 이메일 재전송 시나리오`() {
        val email = "test@goodpon.site"
        val password = "test-password"
        val name = "테스트 계정"

        val (response, _) = 회원가입_요청(email = email, password = password, name = name)
        response.apply { statusCode() shouldBe 200 }
            .toApiResponse<SignUpResult>()
            .apply { verified shouldBe false }

        val invalidEmailToken = "invalid-email-verification-token"
        이메일_인증_요청(emailToken = invalidEmailToken)
            .apply { statusCode() shouldBe 400 }

        val (resendResponse, newEmailToken) = 인증_이메일_재전송_요청(email = email)
        resendResponse.apply { statusCode() shouldBe 200 }

        이메일_인증_요청(emailToken = newEmailToken)
            .apply { statusCode() shouldBe 200 }

        val accessToken = 로그인_요청(email = email, password = password)
            .apply { statusCode() shouldBe 200 }
            .toApiResponse<LoginResult>()
            .accessToken

        내_정보_조회_요청(accessToken = accessToken)
            .apply { statusCode() shouldBe 200 }
            .toApiResponse<SignUpResult>()
            .apply { verified shouldBe true }
    }

    private fun 회원가입_요청(email: String, password: String, name: String): Pair<Response, String> {
        val request = SignUpRequest(email = email, password = password, name = name)

        val result = given()
            .contentType(ContentType.JSON)
            .body(request)
            .`when`()
            .post("/v1/accounts/sign-up")

        return Pair(result, FakeVerificationTokenGenerator.VERIFICATION_TOKEN)
    }

    private fun 로그인_요청(email: String, password: String): Response {
        val request = LoginRequest(
            email = email,
            password = password
        )

        return given()
            .contentType(ContentType.JSON)
            .body(request)
            .`when`()
            .post("/v1/auth/login")
    }

    private fun 인증_이메일_재전송_요청(email: String): Pair<Response, String> {
        val request = ResendVerificationEmailRequest(email = email)

        val response = given()
            .contentType(ContentType.JSON)
            .body(request)
            .`when`()
            .post("/v1/auth/verify/resend")

        return Pair(response, FakeVerificationTokenGenerator.VERIFICATION_TOKEN)
    }

    private fun 이메일_인증_요청(emailToken: String): Response {
        val request = VerifyEmailRequest(token = emailToken)

        return given()
            .contentType(ContentType.JSON)
            .body(request)
            .`when`()
            .post("/v1/auth/verify")
    }

    private fun 내_정보_조회_요청(accessToken: String): Response {
        return given()
            .contentType(ContentType.JSON)
            .withAuthHeader(accessToken)
            .`when`()
            .get("/v1/accounts")

    }
}