package com.goodpon.dashboard.api.web.coupon

import com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema
import com.goodpon.dashboard.api.response.ResultType
import com.goodpon.dashboard.api.support.AbstractDocumentTest
import com.goodpon.dashboard.api.support.WithMockAccount
import com.goodpon.dashboard.application.coupon.service.dto.CouponTemplateSummaries
import com.goodpon.dashboard.application.coupon.service.dto.CouponTemplateSummary
import com.goodpon.dashboard.application.coupon.service.exception.NoMerchantAccessPermissionException
import com.goodpon.dashboard.application.merchant.port.out.exception.MerchantNotFoundException
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

class GetMerchantCouponTemplatesDocumentTest : AbstractDocumentTest() {

    @Test
    @WithMockAccount
    fun `상점의 쿠폰 템플릿 목록 조회 - 성공`() {
        // given
        val couponTemplateSummaries = listOf(
            CouponTemplateSummary(
                id = 2L,
                name = "테스트 쿠폰 2",
                description = "테스트 쿠폰 설명 2",
                status = CouponTemplateStatus.ISSUABLE,
                issueCount = 100,
                redeemCount = 50,
                createdAt = LocalDateTime.of(2025, 7, 14, 10, 0),
            ),
            CouponTemplateSummary(
                id = 1L,
                name = "테스트 쿠폰 1",
                description = "테스트 쿠폰 설명 1",
                status = CouponTemplateStatus.ISSUABLE,
                issueCount = 40,
                redeemCount = 30,
                createdAt = LocalDateTime.of(2025, 7, 13, 10, 0),
            )
        )

        every {
            getMerchantCouponTemplatesUseCase(any(), any())
        } returns CouponTemplateSummaries(templates = couponTemplateSummaries)

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/merchants/{merchantId}/coupon-templates", 1L)
                .withAuthHeader()
        )

        // then
        result.andExpectAll(
            status().isOk,
            jsonPath("$.result").value(ResultType.SUCCESS.name),
            jsonPath("$.error").value(null),
            jsonPath("$.data.templates[0].id").value(2L),
            jsonPath("$.data.templates[0].name").value("테스트 쿠폰 2"),
            jsonPath("$.data.templates[1].id").value(1L),
            jsonPath("$.data.templates[1].name").value("테스트 쿠폰 1"),
        )

        // document
        result.andDocument(
            "상점의 쿠폰 템플릿 목록 조회 - 성공",
            ResourceSnippetParameters.builder()
                .tag("Coupon Template")
                .summary("상점의 쿠폰 템플릿 목록 조회")
                .description("상점의 쿠폰 템플릿 목록 조회 API")
                .requestHeaders(authHeaderFields())
                .pathParameters(
                    parameterWithName("merchantId").description("쿠폰 템플릿이 있는 상점 ID")
                )
                .responseSchema(Schema("ApiResponse<CouponTemplateSummaries>"))
                .responseFields(*getMerchantCouponTemplatesSuccessResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockAccount
    fun `상점의 쿠폰 템플릿 목록 조회 - 실패 - 존재하지 않는 상점`() {
        // given
        every {
            getMerchantCouponTemplatesUseCase(any(), any())
        } throws MerchantNotFoundException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/merchants/{merchantId}/coupon-templates", 1L)
                .withAuthHeader()
                .contentType(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpectAll(
            status().isNotFound,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.data").value(null),
            jsonPath("$.error.code").value("MERCHANT_NOT_FOUND"),
            jsonPath("$.error.message").value("존재하지 않는 상점입니다."),
            jsonPath("$.error.data").value(null)
        )

        // document
        result.andDocument(
            "상점의 쿠폰 템플릿 목록 조회 - 실패 - 존재하지 않는 상점",
            ResourceSnippetParameters.builder()
                .tag("Coupon Template")
                .requestHeaders(authHeaderFields())
                .pathParameters(
                    parameterWithName("merchantId").description("쿠폰 템플릿이 있는 상점 ID")
                )
                .responseFields(*commonFailureResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockAccount
    fun `상점의 쿠폰 템플릿 목록 조회 - 실패 - 상점 접근 권한 없음`() {
        // given
        every {
            getMerchantCouponTemplatesUseCase(any(), any())
        } throws NoMerchantAccessPermissionException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/merchants/{merchantId}/coupon-templates", 1L)
                .withAuthHeader()
                .contentType(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpectAll(
            status().isForbidden,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.data").value(null),
            jsonPath("$.error.code").value("NO_MERCHANT_ACCESS_PERMISSION"),
            jsonPath("$.error.message").value("해당 상점에 접근할 수 있는 권한이 없습니다."),
            jsonPath("$.error.data").value(null)
        )

        // document
        result.andDocument(
            "상점의 쿠폰 템플릿 목록 조회 - 실패 - 상점 접근 권한 없음",
            ResourceSnippetParameters.builder()
                .tag("Coupon Template")
                .requestHeaders(authHeaderFields())
                .pathParameters(
                    parameterWithName("merchantId").description("쿠폰 템플릿이 있는 상점 ID")
                )
                .responseFields(*commonFailureResponseFields().toTypedArray())
                .build()
        )
    }

    private fun getMerchantCouponTemplatesSuccessResponseFields() = commonSuccessResponseFields(
        fieldWithPath("data.templates[].id").type(JsonFieldType.NUMBER).description("쿠폰 템플릿 ID"),
        fieldWithPath("data.templates[].name").type(JsonFieldType.STRING).description("쿠폰 템플릿 이름"),
        fieldWithPath("data.templates[].description").type(JsonFieldType.STRING).description("쿠폰 템플릿 설명"),
        fieldWithPath("data.templates[].status").type(JsonFieldType.STRING).description("쿠폰 템플릿 상태"),
        fieldWithPath("data.templates[].issueCount").type(JsonFieldType.NUMBER).description("발급된 쿠폰 수"),
        fieldWithPath("data.templates[].redeemCount").type(JsonFieldType.NUMBER).description("사용된 쿠폰 수"),
        fieldWithPath("data.templates[].createdAt").type(JsonFieldType.STRING).description("쿠폰 템플릿 생성 일시"),
    )
}