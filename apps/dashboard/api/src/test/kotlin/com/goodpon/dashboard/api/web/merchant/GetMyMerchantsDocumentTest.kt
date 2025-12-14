package com.goodpon.dashboard.api.web.merchant

import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema
import com.goodpon.dashboard.api.response.ResultType
import com.goodpon.dashboard.api.support.AbstractDocumentTest
import com.goodpon.dashboard.api.support.WithMockAccount
import com.goodpon.dashboard.application.merchant.service.dto.MyMerchantSummaries
import com.goodpon.dashboard.application.merchant.service.dto.MyMerchantSummary
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

class GetMyMerchantsDocumentTest : AbstractDocumentTest() {

    @Test
    @WithMockAccount
    fun `내 상점 목록 조회 - 성공`() {
        // given
        val summaries = listOf(
            MyMerchantSummary(
                id = 2L,
                name = "두번째 상점",
                createdAt = LocalDateTime.now()
            ),
            MyMerchantSummary(
                id = 1L,
                name = "첫번째 상점",
                createdAt = LocalDateTime.now().minusDays(1)
            )
        )

        every {
            getMyMerchantsUseCase(any())
        } returns MyMerchantSummaries(merchants = summaries)

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/merchants")
                .withAuthHeader()
        )

        // then
        result.andExpectAll(
            status().isOk,
            jsonPath("$.result").value(ResultType.SUCCESS.name),
            jsonPath("$.error").value(null),
            jsonPath("$.data.merchants.[0].id").value(2L),
            jsonPath("$.data.merchants.[0].name").value("두번째 상점"),
            jsonPath("$.data.merchants.[1].id").value(1L),
            jsonPath("$.data.merchants.[1].name").value("첫번째 상점"),
        )

        // document
        result.andDocument(
            "내 상점 목록 조회 - 성공",
            ResourceSnippetParameters.builder()
                .tag("Merchant")
                .summary("내 상점 목록 조회")
                .description("내 상점 목록 조회 API")
                .requestHeaders(authHeaderFields())
                .responseSchema(Schema("ApiResponse<MyMerchantSummaries>"))
                .responseFields(*getMyMerchantsSuccessFields().toTypedArray())
                .build()
        )
    }

    private fun getMyMerchantsSuccessFields() = commonSuccessResponseFields(
        fieldWithPath("data.merchants[].id").type(JsonFieldType.NUMBER).description("상점 ID"),
        fieldWithPath("data.merchants[].name").type(JsonFieldType.STRING).description("상점 이름"),
        fieldWithPath("data.merchants[].createdAt").type(JsonFieldType.STRING).description("상점 생성 시간")
    )
}