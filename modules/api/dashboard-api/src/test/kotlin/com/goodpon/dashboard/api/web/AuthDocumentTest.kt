package com.goodpon.dashboard.api.web

import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema
import com.goodpon.dashboard.api.controller.v1.auth.dto.LoginRequest
import com.goodpon.dashboard.api.controller.v1.auth.dto.ResendVerificationEmailRequest
import com.goodpon.dashboard.api.controller.v1.auth.dto.VerifyEmailRequest
import com.goodpon.dashboard.api.response.ResultType
import com.goodpon.dashboard.api.support.AbstractDocumentTest
import com.goodpon.dashboard.application.account.port.out.exception.AccountNotFoundException
import com.goodpon.dashboard.application.auth.port.`in`.dto.LoginResult
import com.goodpon.dashboard.application.auth.service.exception.EmailVerificationNotFoundException
import com.goodpon.dashboard.application.auth.service.exception.PasswordMismatchException
import com.goodpon.domain.account.exception.AccountAlreadyVerifiedException
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AuthDocumentTest : AbstractDocumentTest() {

    @Test
    fun `로그인 - 성공`() {
        // given
        val request = LoginRequest(email = "test@goodpon.site", password = "password123")
        val accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6Ikpva" +
                "G4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30"
        val loginResult = LoginResult(
            id = 1L,
            email = "test@goodpon.site",
            name = "테스트 사용자",
            verified = true,
            accessToken = accessToken
        )

        every { loginUseCase(any()) } returns loginResult

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        // then
        result.andExpectAll(
            status().isOk,
            jsonPath("$.result").value(ResultType.SUCCESS.name),
            jsonPath("$.error").value(null),
            jsonPath("$.data.id").value(1L),
            jsonPath("$.data.email").value("test@goodpon.site"),
            jsonPath("$.data.name").value("테스트 사용자"),
            jsonPath("$.data.verified").value(true),
            jsonPath("$.data.accessToken").value(accessToken)
        )

        // document
        result.andDocument(
            "로그인 - 성공",
            ResourceSnippetParameters.builder()
                .tag("Auth")
                .summary("로그인")
                .description("로그인 API")
                .requestSchema(Schema("LoginRequest"))
                .requestFields(*loginRequestFields().toTypedArray())
                .responseSchema(Schema("ApiResponse<LoginResult>"))
                .responseFields(*loginSuccessResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    fun `로그인 - 실패 - 존재하지 않는 계정`() {
        // given
        val request = LoginRequest(email = "noExists@goodpon.site", password = "password123")

        every { loginUseCase(any()) } throws AccountNotFoundException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        // then
        result.andExpectAll(
            status().isNotFound,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.data").value(null),
            jsonPath("$.error.code").value("ACCOUNT_NOT_FOUND"),
            jsonPath("$.error.message").value("존재하지 않는 계정입니다."),
            jsonPath("$.error.data").value(null)
        )

        // document
        result.andDocument(
            "로그인 - 실패 - 존재하지 않는 계정",
            ResourceSnippetParameters.builder()
                .tag("Auth")
                .requestSchema(Schema("LoginRequest"))
                .requestFields(*loginRequestFields().toTypedArray())
                .responseFields(*commonFailureResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    fun `로그인 - 실패 - 비밀번호 불일치`() {
        // given
        val request = LoginRequest(email = "test@goodpon.site", password = "wrong password")

        every { loginUseCase(any()) } throws PasswordMismatchException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        // then
        result.andExpectAll(
            status().isUnauthorized,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.data").value(null),
            jsonPath("$.error.code").value("PASSWORD_MISMATCH"),
            jsonPath("$.error.message").value("비밀번호가 일치하지 않습니다."),
            jsonPath("$.error.data").value(null)
        )

        // document
        result.andDocument(
            "로그인 - 실패 - 비밀번호 불일치",
            ResourceSnippetParameters.builder()
                .tag("Auth")
                .requestSchema(Schema("LoginRequest"))
                .requestFields(*loginRequestFields().toTypedArray())
                .responseFields(*commonFailureResponseFields().toTypedArray())
                .build()
        )
    }

    private fun loginRequestFields() = listOf(
        fieldWithPath("email").type(JsonFieldType.STRING).description("계정 이메일"),
        fieldWithPath("password").type(JsonFieldType.STRING).description("계정 비밀번호"),
    )

    private fun loginSuccessResponseFields() = commonSuccessResponseFields(
        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("사용자 ID"),
        fieldWithPath("data.email").type(JsonFieldType.STRING).description("사용자 이메일"),
        fieldWithPath("data.name").type(JsonFieldType.STRING).description("사용자 이름"),
        fieldWithPath("data.verified").type(JsonFieldType.BOOLEAN).description("이메일 인증 여부"),
        fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
    )

    @Test
    fun `이메일 인증 - 성공`() {
        // given
        val token = "email-verification-token"
        val request = VerifyEmailRequest(token = token)

        every { verifyEmailUseCase(any()) } returns Unit

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/auth/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        // then
        result.andExpectAll(
            status().isOk,
            jsonPath("$.result").value(ResultType.SUCCESS.name),
            jsonPath("$.error").value(null),
            jsonPath("$.data").value("이메일 인증이 완료되었습니다.")
        )

        // document
        result.andDocument(
            "이메일 인증 - 성공",
            ResourceSnippetParameters.builder()
                .tag("Auth")
                .summary("이메일 인증")
                .description("이메일 인증 API")
                .requestSchema(Schema("VerifyEmailRequest"))
                .requestFields(*verifyEmailRequestFields().toTypedArray())
                .responseFields(*verifyEmailResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    fun `이메일 인증 - 실패 - 유효하지 않는 이메일 인증 토큰`() {
        // given
        val token = "invalid-email-verification-token"
        val request = VerifyEmailRequest(token = token)

        every { verifyEmailUseCase(any()) } throws EmailVerificationNotFoundException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/auth/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        // then
        result.andExpectAll(
            status().isBadRequest,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("INVALID_EMAIL_VERIFICATION_TOKEN"),
            jsonPath("$.error.message").value("유효하지 않은 이메일 인증 토큰입니다."),
            jsonPath("$.error.data").value(null),
            jsonPath("$.data").value(null)
        )

        // document
        result.andDocument(
            "이메일 인증 - 실패 - 유효하지 않는 이메일 인증 토큰",
            ResourceSnippetParameters.builder()
                .tag("Auth")
                .requestSchema(Schema("VerifyEmailRequest"))
                .requestFields(*verifyEmailRequestFields().toTypedArray())
                .responseFields(*commonFailureResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    fun `이메일 인증 - 실패 - 존재하지 않는 계정`() {
        // given
        val token = "invalid-email-verification-token"
        val request = VerifyEmailRequest(token = token)

        every { verifyEmailUseCase(any()) } throws AccountNotFoundException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/auth/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        // then
        result.andExpectAll(
            status().isNotFound,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("ACCOUNT_NOT_FOUND"),
            jsonPath("$.error.message").value("존재하지 않는 계정입니다."),
            jsonPath("$.error.data").value(null),
            jsonPath("$.data").value(null)
        )

        // document
        result.andDocument(
            "이메일 인증 - 실패 - 존재하지 않는 계정",
            ResourceSnippetParameters.builder()
                .tag("Auth")
                .requestSchema(Schema("VerifyEmailRequest"))
                .requestFields(*verifyEmailRequestFields().toTypedArray())
                .responseFields(*commonFailureResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    fun `이메일 인증 - 실패 - 이미 인증된 계정`() {
        // given
        val token = "invalid-email-verification-token"
        val request = VerifyEmailRequest(token = token)

        every { verifyEmailUseCase(any()) } throws AccountAlreadyVerifiedException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/auth/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        // then
        result.andExpectAll(
            status().isBadRequest,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("ACCOUNT_ALREADY_VERIFIED"),
            jsonPath("$.error.message").value("이미 인증된 계정입니다."),
            jsonPath("$.error.data").value(null),
            jsonPath("$.data").value(null)
        )

        // document
        result.andDocument(
            "이메일 인증 - 실패 - 이미 인증된 계정",
            ResourceSnippetParameters.builder()
                .tag("Auth")
                .requestSchema(Schema("VerifyEmailRequest"))
                .requestFields(*verifyEmailRequestFields().toTypedArray())
                .responseFields(*commonFailureResponseFields().toTypedArray())
                .build()
        )
    }

    private fun verifyEmailRequestFields() = listOf(
        fieldWithPath("token").type(JsonFieldType.STRING).description("이메일 인증 토큰")
    )

    private fun verifyEmailResponseFields() = listOf(
        fieldWithPath("result").type(JsonFieldType.STRING).description("요청 결과 (SUCCESS/ERROR)"),
        fieldWithPath("data").type(JsonFieldType.STRING).description("이메일 인증 완료 메시지"),
        fieldWithPath("error").type(JsonFieldType.NULL).description("오류 정보 (성공시 null)")
    )

    @Test
    fun `인증 이메일 재전송 - 성공`() {
        // given
        val request = ResendVerificationEmailRequest(email = "test@goodpon.site")

        every { resendVerificationEmailUseCase(any()) } returns Unit

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/auth/verify/resend")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        // then
        result.andExpectAll(
            status().isOk,
            jsonPath("$.result").value(ResultType.SUCCESS.name),
            jsonPath("$.error").value(null),
            jsonPath("$.data").value("인증 이메일이 재전송되었습니다.")
        )

        // document
        result.andDocument(
            "인증 이메일 재전송 - 성공",
            ResourceSnippetParameters.builder()
                .tag("Auth")
                .summary("인증 이메일 재전송")
                .description("인증 이메일 재전송 API")
                .requestSchema(Schema("ResendVerificationEmailRequest"))
                .requestFields(*resendVerificationEmailRequestFields().toTypedArray())
                .responseFields(*resendVerificationEmailResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    fun `인증 이메일 재전송 - 실패 - 존재하지 않는 이메일`() {
        // given
        val request = ResendVerificationEmailRequest(email = "test@goodpon.site")

        every { resendVerificationEmailUseCase(any()) } throws AccountNotFoundException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/auth/verify/resend")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        // then
        result.andExpectAll(
            status().isNotFound,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("ACCOUNT_NOT_FOUND"),
            jsonPath("$.error.message").value("존재하지 않는 계정입니다."),
            jsonPath("$.error.data").value(null),
            jsonPath("$.data").value(null)
        )

        // document
        result.andDocument(
            "인증 이메일 재전송 - 실패 - 존재하지 않는 이메일",
            ResourceSnippetParameters.builder()
                .tag("Auth")
                .requestSchema(Schema("ResendVerificationEmailRequest"))
                .requestFields(*resendVerificationEmailRequestFields().toTypedArray())
                .responseFields(*commonFailureResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    fun `인증 이메일 재전송 - 실패 - 이미 인증된 이메일`() {
        // given
        val request = ResendVerificationEmailRequest(email = "test@goodpon.site")

        every { resendVerificationEmailUseCase(any()) } throws AccountAlreadyVerifiedException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/auth/verify/resend")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        // then
        result.andExpectAll(
            status().isBadRequest,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("ACCOUNT_ALREADY_VERIFIED"),
            jsonPath("$.error.message").value("이미 인증된 계정입니다."),
            jsonPath("$.error.data").value(null),
            jsonPath("$.data").value(null)
        )

        // document
        result.andDocument(
            "인증 이메일 재전송 - 실패 - 이미 인증된 이메일",
            ResourceSnippetParameters.builder()
                .tag("Auth")
                .requestSchema(Schema("ResendVerificationEmailRequest"))
                .requestFields(*resendVerificationEmailRequestFields().toTypedArray())
                .responseFields(*commonFailureResponseFields().toTypedArray())
                .build()
        )
    }

    private fun resendVerificationEmailRequestFields() = listOf(
        fieldWithPath("email").type(JsonFieldType.STRING).description("인증 이메일을 재전송할 계정의 이메일")
    )

    private fun resendVerificationEmailResponseFields() = listOf(
        fieldWithPath("result").type(JsonFieldType.STRING).description("요청 결과 (SUCCESS/ERROR)"),
        fieldWithPath("data").type(JsonFieldType.STRING).description("인증 이메일 재전송 완료 메시지"),
        fieldWithPath("error").type(JsonFieldType.NULL).description("오류 정보 (성공시 null)")
    )
}