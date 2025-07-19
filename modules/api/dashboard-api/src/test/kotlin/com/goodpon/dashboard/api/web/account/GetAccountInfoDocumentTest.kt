package com.goodpon.dashboard.api.web.account

import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema
import com.goodpon.dashboard.api.response.ResultType
import com.goodpon.dashboard.api.support.AbstractDocumentTest
import com.goodpon.dashboard.api.support.WithMockAccount
import com.goodpon.dashboard.application.account.port.`in`.dto.AccountInfo
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class GetAccountInfoDocumentTest : AbstractDocumentTest() {

    @Test
    @WithMockAccount
    fun `내 계정 정보 조회 - 성공`() {
        // given
        val accountInfo = AccountInfo(
            id = 1L,
            email = "test@goodpon.site",
            name = "테스트 사용자",
            verified = true
        )

        every { getAccountInfoUseCase(any()) } returns accountInfo

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/accounts")
                .withAuthHeader()
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
        )

        // document
        result.andDocument(
            "내 계정 정보 조회 - 성공",
            ResourceSnippetParameters.builder()
                .tag("Account")
                .summary("내 계정 정보 조회")
                .description("내 계정 조회 API")
                .requestHeaders(authHeaderFields())
                .responseSchema(Schema("ApiResponse<AccountInfo>"))
                .responseFields(*getAccountInfoSuccessResponseFields().toTypedArray())
                .build()
        )
    }

    private fun getAccountInfoSuccessResponseFields() = commonSuccessResponseFields(
        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("사용자 ID"),
        fieldWithPath("data.email").type(JsonFieldType.STRING).description("사용자 이메일"),
        fieldWithPath("data.name").type(JsonFieldType.STRING).description("사용자 이름"),
        fieldWithPath("data.verified").type(JsonFieldType.BOOLEAN).description("이메일 인증 여부"),
    )
}