package com.goodpon.api.dashboard.web.account

import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema
import com.goodpon.api.dashboard.controller.v1.account.dto.SignUpRequest
import com.goodpon.api.dashboard.response.ResultType
import com.goodpon.api.dashboard.support.AbstractDocumentTest
import com.goodpon.application.dashboard.account.port.`in`.dto.SignUpResult
import com.goodpon.application.dashboard.account.service.exception.AccountEmailExistsException
import com.goodpon.domain.account.exception.AccountInvalidEmailFormatException
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class SignUpDocumentTest : AbstractDocumentTest() {

    @Test
    fun `회원가입 - 성공`() {
        // given
        val request = SignUpRequest(email = "test@goodpon.site", password = "password123", name = "테스트 사용자")
        val signUpResult = SignUpResult(id = 1L, email = request.email, name = request.name, verified = false)

        every { signUpUseCase(any()) } returns signUpResult

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/accounts/sign-up")
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
            jsonPath("$.data.verified").value(false),
        )

        // document
        result.andDocument(
            "회원가입 - 성공",
            ResourceSnippetParameters.builder()
                .tag("Account")
                .summary("회원 가입")
                .description("회원 가입 API")
                .requestSchema(Schema("SignUpRequest"))
                .requestFields(*signUpRequestFields().toTypedArray())
                .responseSchema(Schema("ApiResponse<SignUpResult>"))
                .responseFields(*signUpSuccessResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    fun `회원가입 - 실패 - 이미 사용 중인 이메일`() {
        // given
        val request = SignUpRequest(
            email = "existsEmail@goodpon.site",
            password = "password123",
            name = "테스트 사용자"
        )

        every { signUpUseCase(any()) } throws AccountEmailExistsException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/accounts/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        // then
        result.andExpectAll(
            status().isBadRequest,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.data").value(null),
            jsonPath("$.error.code").value("ACCOUNT_EMAIL_ALREADY_EXISTS"),
            jsonPath("$.error.message").value("이미 사용 중인 이메일입니다."),
            jsonPath("$.error.data").value(null)
        )

        // document
        result.andDocument(
            "회원가입 - 실패 - 이미 사용 중인 이메일",
            ResourceSnippetParameters.builder()
                .tag("Account")
                .requestSchema(Schema("SignUpRequest"))
                .requestFields(*signUpRequestFields().toTypedArray())
                .responseSchema(Schema("ApiResponse<Unit>"))
                .responseFields(*commonFailureResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    fun `회원가입 - 실패 - 잘못된 입력값`() {
        // given
        val request = SignUpRequest(
            email = "invalid-email-format",
            password = "password123",
            name = "테스트 사용자"
        )

        every { signUpUseCase(any()) } throws AccountInvalidEmailFormatException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/accounts/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        // then
        result.andExpectAll(
            status().isBadRequest,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.data").value(null),
            jsonPath("$.error.code").value("ACCOUNT_SIGN_UP_INVALID_INPUT"),
            jsonPath("$.error.message").value("계정 생성에 필요한 입력값이 올바르지 않습니다."),
            jsonPath("$.error.data").value(null)
        )

        // document
        result.andDocument(
            "회원가입 - 실패 - 잘못된 입력값",
            ResourceSnippetParameters.builder()
                .tag("Account")
                .requestSchema(Schema("SignUpRequest"))
                .requestFields(*signUpRequestFields().toTypedArray())
                .responseSchema(Schema("ApiResponse<Unit>"))
                .responseFields(*commonFailureResponseFields().toTypedArray())
                .build()
        )
    }

    private fun signUpRequestFields() = listOf(
        fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일"),
        fieldWithPath("password").type(JsonFieldType.STRING).description("사용자 비밀번호"),
        fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 이름")
    )

    private fun signUpSuccessResponseFields() = commonSuccessResponseFields(
        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("사용자 ID"),
        fieldWithPath("data.email").type(JsonFieldType.STRING).description("사용자 이메일"),
        fieldWithPath("data.name").type(JsonFieldType.STRING).description("사용자 이름"),
        fieldWithPath("data.verified").type(JsonFieldType.BOOLEAN).description("이메일 인증 여부")
    )

}