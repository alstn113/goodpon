package com.goodpon.dashboard.api.web.auth

import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema
import com.goodpon.dashboard.api.controller.v1.auth.dto.VerifyEmailRequest
import com.goodpon.dashboard.api.response.ResultType
import com.goodpon.dashboard.api.support.AbstractDocumentTest
import com.goodpon.dashboard.application.account.port.out.exception.AccountNotFoundException
import com.goodpon.dashboard.application.auth.service.exception.EmailVerificationNotFoundException
import com.goodpon.domain.account.exception.AccountAlreadyVerifiedException
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class VerifyEmailDocumentTest : AbstractDocumentTest() {

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
                .responseSchema(Schema("ApiResponse<Unit>"))
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
                .responseSchema(Schema("ApiResponse<Unit>"))
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
                .responseSchema(Schema("ApiResponse<Unit>"))
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
                .responseSchema(Schema("ApiResponse<Unit>"))
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
}