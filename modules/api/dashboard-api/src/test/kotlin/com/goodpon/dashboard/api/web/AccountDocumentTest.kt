package com.goodpon.dashboard.api.web

import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema
import com.goodpon.dashboard.api.controller.v1.account.dto.SignUpRequest
import com.goodpon.dashboard.api.response.ResultType
import com.goodpon.dashboard.api.support.AbstractDocumentTest
import com.goodpon.dashboard.application.account.port.`in`.dto.SignUpResult
import com.goodpon.dashboard.application.account.service.exception.AccountEmailExistsException
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
    fun sign_up_success() {
        // given
        val request = SignUpRequest(email = "test@goodpon.site", password = "password123", name = "테스트 사용자")
        val signUpResult = SignUpResult(id = 1L, email = request.email, name = request.name, verified = false)

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
            jsonPath("$.result").value(ResultType.SUCCESS.name),
            jsonPath("$.error").value(null),
            jsonPath("$.data.id").value(1L),
            jsonPath("$.data.email").value("test@goodpon.site"),
            jsonPath("$.data.name").value("테스트 사용자"),
            jsonPath("$.data.verified").value(false),
        )

        // document
        result.andDocument(
            "성공",
            ResourceSnippetParameters.builder()
                .tag("Account")
                .summary("회원 가입")
                .description("회원 가입을 위한 API입니다. 사용자의 이메일, 비밀번호, 이름을 입력받아 회원 가입을 처리합니다.")
                .requestSchema(Schema("ApiRequest<SignUpRequest>"))
                .requestFields(*signUpRequestFields().toTypedArray())
                .responseSchema(Schema("ApiResponse<SignUpResult>"))
                .responseFields(*signUpSuccessResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    fun sign_up_failure_exists_email() {
        // given
        val request = SignUpRequest(
            email = "existsEmail@goodpon.site",
            password = "password123",
            name = "테스트 사용자"
        )

        every { signUpUseCase.signUp(any()) } throws AccountEmailExistsException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/account/sign-up")
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
            "실패 - 이미 사용 중인 이메일",
            ResourceSnippetParameters.builder()
                .tag("Account")
                .summary("회원 가입 실패 - 이미 사용 중인 이메일")
                .description("이메일은 고유해야 하며, 이미 등록된 이메일로 회원 가입을 시도할 수 없습니다.")
                .requestSchema(Schema("ApiResponse<SignUpRequest>"))
                .requestFields(*signUpRequestFields().toTypedArray())
                .responseSchema(Schema("ApiResponse<Unit>"))
                .responseFields(* signUpFailureResponseFields().toTypedArray())
                .build()
        )
    }

    private fun signUpRequestFields() = listOf(
        fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일"),
        fieldWithPath("password").type(JsonFieldType.STRING).description("사용자 비밀번호"),
        fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 이름")
    )

    private fun signUpSuccessResponseFields() = listOf(
        fieldWithPath("result").type(JsonFieldType.STRING).description("요청 결과 (SUCCESS/ERROR)"),
        fieldWithPath("data").type(JsonFieldType.OBJECT).description("회원가입 결과 데이터"),
        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("사용자 ID"),
        fieldWithPath("data.email").type(JsonFieldType.STRING).description("사용자 이메일"),
        fieldWithPath("data.name").type(JsonFieldType.STRING).description("사용자 이름"),
        fieldWithPath("data.verified").type(JsonFieldType.BOOLEAN).description("이메일 인증 여부"),
        fieldWithPath("error").type(JsonFieldType.NULL).description("오류 정보 (성공시 null)")
    )

    private fun signUpFailureResponseFields() = listOf(
        fieldWithPath("result").type(JsonFieldType.STRING).description("요청 결과 (SUCCESS/ERROR)"),
        fieldWithPath("data").type(JsonFieldType.NULL).description("회원가입 결과 데이터 (실패 시 null)"),
        fieldWithPath("error").type(JsonFieldType.OBJECT).description("오류 정보"),
        fieldWithPath("error.code").type(JsonFieldType.STRING).description("오류 코드"),
        fieldWithPath("error.message").type(JsonFieldType.STRING).description("오류 메시지"),
        fieldWithPath("error.data").type(JsonFieldType.NULL).description("오류 데이터 (없을 경우 null)")
    )
}