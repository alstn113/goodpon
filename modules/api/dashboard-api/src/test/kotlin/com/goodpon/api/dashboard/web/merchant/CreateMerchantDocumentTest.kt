package com.goodpon.api.dashboard.web.merchant

import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema
import com.goodpon.api.dashboard.controller.v1.merchant.dto.CreateMerchantRequest
import com.goodpon.api.dashboard.response.ResultType
import com.goodpon.api.dashboard.support.AbstractDocumentTest
import com.goodpon.api.dashboard.support.WithMockAccount
import com.goodpon.application.dashboard.merchant.port.`in`.dto.CreateMerchantResult
import com.goodpon.domain.merchant.MerchantAccountRole
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class CreateMerchantDocumentTest : AbstractDocumentTest() {

    @Test
    @WithMockAccount
    fun `상점 생성 - 성공`() {
        // given
        val request = CreateMerchantRequest(name = "상점")
        val createMerchantResult = CreateMerchantResult(
            id = 1L,
            name = "상점",
            clientId = "ck_dd144f3b95144877a0dcbaad2b321380",
            clientSecrets = listOf(
                CreateMerchantResult.ClientSecretInfo(
                    id = 1L,
                    secret = "sk_8f3b95144877a0dcbaad2b321380",
                    expiredAt = null
                )
            ),
            merchantAccounts = listOf(
                CreateMerchantResult.MerchantAccountInfo(
                    id = 1L,
                    accountId = 1L,
                    role = MerchantAccountRole.OWNER
                )
            )
        )

        every { createMerchantUseCase(any()) } returns createMerchantResult

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/merchants")
                .withAuthHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        // then
        result.andExpectAll(
            status().isOk,
            jsonPath("$.result").value(ResultType.SUCCESS.name),
            jsonPath("$.error").value(null),
            jsonPath("$.data.id").value(1L),
            jsonPath("$.data.name").value("상점"),
            jsonPath("$.data.clientId").value("ck_dd144f3b95144877a0dcbaad2b321380"),
            jsonPath("$.data.clientSecrets[0].id").value(1L),
            jsonPath("$.data.clientSecrets[0].secret").value("sk_8f3b95144877a0dcbaad2b321380"),
            jsonPath("$.data.clientSecrets[0].expiredAt").value(null),
            jsonPath("$.data.merchantAccounts[0].id").value(1L),
            jsonPath("$.data.merchantAccounts[0].accountId").value(1L),
            jsonPath("$.data.merchantAccounts[0].role").value(MerchantAccountRole.OWNER.name)
        )

        // document
        result.andDocument(
            "상점 생성 - 성공",
            ResourceSnippetParameters.builder()
                .tag("Merchant")
                .summary("상점 생성")
                .description("상점 생성 API")
                .requestHeaders(authHeaderFields())
                .requestSchema(Schema("CreateMerchantRequest"))
                .requestFields(*createMerchantRequestFields().toTypedArray())
                .responseSchema(Schema("ApiResponse<CreateMerchantResult>"))
                .responseFields(*createMerchantSuccessFields().toTypedArray())
                .build()
        )
    }

    private fun createMerchantRequestFields() = listOf(
        fieldWithPath("name").type(JsonFieldType.STRING).description("상점 이름")
    )

    private fun createMerchantSuccessFields() = commonSuccessResponseFields(
        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("상점 ID"),
        fieldWithPath("data.name").type(JsonFieldType.STRING).description("상점 이름"),
        fieldWithPath("data.clientId").type(JsonFieldType.STRING).description("상점 클라이언트 ID"),
        fieldWithPath("data.clientSecrets").type(JsonFieldType.ARRAY).description("상점 클라이언트 시크릿 목록"),
        fieldWithPath("data.clientSecrets[].id").type(JsonFieldType.NUMBER).description("클라이언트 시크릿 ID"),
        fieldWithPath("data.clientSecrets[].secret").type(JsonFieldType.STRING).description("클라이언트 시크릿 값"),
        fieldWithPath("data.clientSecrets[].expiredAt").type(JsonFieldType.NULL).optional()
            .description("키가 만료된 시간 (유효한 경우 null)"),
        fieldWithPath("data.merchantAccounts").type(JsonFieldType.ARRAY).description("상점 계정 목록"),
        fieldWithPath("data.merchantAccounts[].id").type(JsonFieldType.NUMBER).description("상점 계정 ID"),
        fieldWithPath("data.merchantAccounts[].accountId").type(JsonFieldType.NUMBER).description("상점 계정의 사용자 ID"),
        fieldWithPath("data.merchantAccounts[].role").type(JsonFieldType.STRING).description("상점 계정의 역할 (OWNER, STAFF)")
    )
}