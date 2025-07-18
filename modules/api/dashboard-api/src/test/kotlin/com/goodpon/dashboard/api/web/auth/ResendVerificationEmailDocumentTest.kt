package com.goodpon.dashboard.api.web.auth

import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema
import com.goodpon.dashboard.api.controller.v1.auth.dto.ResendVerificationEmailRequest
import com.goodpon.dashboard.api.response.ResultType
import com.goodpon.dashboard.api.support.AbstractDocumentTest
import com.goodpon.dashboard.application.account.port.out.exception.AccountNotFoundException
import com.goodpon.domain.account.exception.AccountAlreadyVerifiedException
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ResendVerificationEmailDocumentTest : AbstractDocumentTest() {

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
                .responseSchema(Schema("ApiResponse<Unit>"))
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
                .responseSchema(Schema("ApiResponse<Unit>"))
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
                .responseSchema(Schema("ApiResponse<Unit>"))
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