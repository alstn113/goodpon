package com.goodpon.dashboard.api.web

import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema
import com.goodpon.dashboard.api.controller.v1.account.dto.SignUpRequest
import com.goodpon.dashboard.api.support.AbstractDocumentTest
import com.goodpon.dashboard.application.account.port.`in`.dto.SignUpResult
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AccountDocumentTest : AbstractDocumentTest() {

    @Test
    fun `signUp`() {
        // given
        val request = SignUpRequest(
            email = "test@goodpon.site",
            password = "password123",
            name = "테스트 사용자"
        )
        val signUpResult = SignUpResult(
            id = 1L,
            email = request.email,
            name = request.name,
            verified = false
        )
        every { signUpUseCase.signUp(any()) } returns signUpResult

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/account/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        // then
        result.andExpectAll(
            status().isOk,
            jsonPath("$.result").value("SUCCESS"),
            jsonPath("$.errorMessage").value(null),
            jsonPath("$.data.id").value(1L),
            jsonPath("$.data.email").value("test@goodpon.site"),
            jsonPath("$.data.name").value("테스트 사용자"),
            jsonPath("$.data.verified").value(false),
        )

        // document
        result.andDocument(
            "Sign Up",
            ResourceSnippetParameters.builder()
                .requestSchema(Schema("SignUpRequest"))
                .responseSchema(Schema("SignUpResult"))
                .description("회원 가입 API")
                .requestFields(
                    fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일"),
                    fieldWithPath("password").type(JsonFieldType.STRING).description("사용자 비밀번호"),
                    fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 이름")
                )
                .responseFields(
                    fieldWithPath("result").type(JsonFieldType.STRING).description("요청 결과"),
                    fieldWithPath("errorMessage").type(JsonFieldType.STRING).optional().description("오류 메시지"),
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("사용자 ID"),
                    fieldWithPath("data.email").type(JsonFieldType.STRING).description("사용자 이메일"),
                    fieldWithPath("data.name").type(JsonFieldType.STRING).description("사용자 이름"),
                    fieldWithPath("data.verified").type(JsonFieldType.BOOLEAN).description("이메일 인증 여부")
                )
                .build()
        )
    }
}