package com.goodpon.dashboard.api.web.merchant

import com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema
import com.goodpon.dashboard.api.response.ResultType
import com.goodpon.dashboard.api.support.AbstractDocumentTest
import com.goodpon.dashboard.api.support.WithMockAccount
import com.goodpon.dashboard.application.merchant.port.out.exception.MerchantNotFoundException
import com.goodpon.dashboard.application.merchant.service.dto.MyMerchantDetail
import com.goodpon.domain.merchant.MerchantAccountRole
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

class GetMyMerchantDetailDocumentTest : AbstractDocumentTest() {

    @Test
    @WithMockAccount
    fun `상점 상세 조회 - 성공`() {
        // given
        val detail = MyMerchantDetail(
            id = 1L,
            name = "테스트 상점",
            clientId = "ck_dd144f3b95144877a0dcbaad2b321380",
            merchantAccounts = listOf(
                MyMerchantDetail.MerchantAccountDetail(
                    merchantAccountId = 1L,
                    accountId = 1L,
                    email = "test@goodpon.site",
                    name = "테스트 사용자",
                    role = MerchantAccountRole.OWNER,
                    createdAt = LocalDateTime.now()
                )
            ),
            clientSecrets = listOf(
                MyMerchantDetail.MerchantClientSecretDetail(
                    merchantClientSecretId = 1L,
                    secret = "sk_8f3b95144877a0dcbaad2b321380",
                    createdAt = LocalDateTime.now(),
                    expiredAt = null
                )
            ),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        every { getMyMerchantDetailUseCase(any(), any()) } returns detail

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/merchants/{merchantId}", 1L)
                .withAuthHeader()
        )

        // then
        result.andExpectAll(
            status().isOk,
            jsonPath("$.result").value(ResultType.SUCCESS.name),
            jsonPath("$.error").value(null),
            jsonPath("$.data.id").value(1L),
            jsonPath("$.data.name").value("테스트 상점"),
            jsonPath("$.data.clientId").value("ck_dd144f3b95144877a0dcbaad2b321380"),
            jsonPath("$.data.merchantAccounts[0].merchantAccountId").value(1L),
            jsonPath("$.data.clientSecrets[0].merchantClientSecretId").value(1L),
            jsonPath("$.data.clientSecrets[0].secret").value("sk_8f3b95144877a0dcbaad2b321380"),
            jsonPath("$.data.clientSecrets[0].expiredAt").value(null),
        )

        // document
        result.andDocument(
            "상점 상세 조회 - 성공",
            ResourceSnippetParameters.builder()
                .tag("Merchant")
                .summary("상점 상세 조회")
                .description("상점 상세 정보 조회 API")
                .requestHeaders(authHeaderFields())
                .pathParameters(parameterWithName("merchantId").description("조회할 상점 ID"))
                .responseSchema(Schema("ApiResponse<MyMerchantDetail>"))
                .responseFields(*getMyMerchantDetailSuccessFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockAccount
    fun `상점 상세 조회 - 실패 - 존재하지 않는 상점`() {
        // given
        every { getMyMerchantDetailUseCase(any(), any()) } throws MerchantNotFoundException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/merchants/{merchantId}", 1L)
                .withAuthHeader()
        )

        // then
        result.andExpectAll(
            status().isNotFound,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("MERCHANT_NOT_FOUND"),
            jsonPath("$.error.message").value("존재하지 않는 상점입니다."),
            jsonPath("$.error.data").value(null),
            jsonPath("$.data").value(null)
        )

        // document
        result
            .andDocument(
                "상점 상세 조회 - 실패 - 존재하지 않는 상점",
                ResourceSnippetParameters.builder()
                    .tag("Merchant")
                    .requestHeaders(authHeaderFields())
                    .pathParameters(parameterWithName("merchantId").description("조회할 상점 ID"))
                    .responseFields(*commonFailureResponseFields().toTypedArray())
                    .build()
            )
    }

    private fun getMyMerchantDetailSuccessFields() = commonSuccessResponseFields(
        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("상점 ID"),
        fieldWithPath("data.name").type(JsonFieldType.STRING).description("상점 이름"),
        fieldWithPath("data.clientId").type(JsonFieldType.STRING).description("상점 클라이언트 ID"),
        fieldWithPath("data.merchantAccounts").type(JsonFieldType.ARRAY).description("상점 계정 목록"),
        fieldWithPath("data.merchantAccounts[].merchantAccountId").type(JsonFieldType.NUMBER)
            .description("상점 계정 ID"),
        fieldWithPath("data.merchantAccounts[].accountId").type(JsonFieldType.NUMBER)
            .description("상점 계정의 사용자 ID"),
        fieldWithPath("data.merchantAccounts[].email").type(JsonFieldType.STRING)
            .description("상점 계정의 사용자 이메일"),
        fieldWithPath("data.merchantAccounts[].name").type(JsonFieldType.STRING)
            .description("상점 계정의 사용자 이름"),
        fieldWithPath("data.merchantAccounts[].role").type(JsonFieldType.STRING)
            .description("상점 계정의 역할 (OWNER, STAFF)"),
        fieldWithPath("data.merchantAccounts[].createdAt").type(JsonFieldType.STRING)
            .description("상점 계정 생성 시간"),
        fieldWithPath("data.clientSecrets").type(JsonFieldType.ARRAY).description("상점 클라이언트 시크릿 목록"),
        fieldWithPath("data.clientSecrets[].merchantClientSecretId").type(JsonFieldType.NUMBER)
            .description("상점 클라이언트 시크릿 ID"),
        fieldWithPath("data.clientSecrets[].secret").type(JsonFieldType.STRING)
            .description("상점 클라이언트 시크릿 값"),
        fieldWithPath("data.clientSecrets[].createdAt").type(JsonFieldType.STRING)
            .description("상점 클라이언트 시크릿 생성 시간"),
        fieldWithPath("data.clientSecrets[].expiredAt").type(JsonFieldType.NULL).optional()
            .description("키가 만료된 시간 (유효한 경우 null)"),
        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("상점 생성 시간"),
        fieldWithPath("data.updatedAt").type(JsonFieldType.STRING).description("상점 정보 수정 시간")
    )
}