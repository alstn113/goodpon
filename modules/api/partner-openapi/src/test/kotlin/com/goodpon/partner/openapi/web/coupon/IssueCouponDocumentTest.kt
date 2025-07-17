package com.goodpon.partner.openapi.web.coupon

import com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema
import com.goodpon.partner.application.coupon.port.`in`.dto.IssueCouponResult
import com.goodpon.partner.openapi.controller.v1.request.IssueCouponRequest
import com.goodpon.partner.openapi.response.ResultType
import com.goodpon.partner.openapi.support.AbstractDocumentTest
import com.goodpon.partner.openapi.support.WithMockMerchant
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

class IssueCouponDocumentTest : AbstractDocumentTest() {

    @Test
    @WithMockMerchant
    fun `쿠폰 발급 - 성공`() {
        val userId = "partner-unique-user-id"
        val request = IssueCouponRequest(
            userId = userId,
        )
        val issueCouponResult = IssueCouponResult(
            userCouponId = "8f3b95144877a0dcbaad2b321380",
            userId = userId,
            couponTemplateId = 1L,
            couponTemplateName = "테스트 쿠폰",
            issuedAt = LocalDateTime.now(),
            expiresAt = LocalDateTime.now().plusDays(30),
        )

        every { issueCouponUseCase(any()) } returns issueCouponResult

        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/coupon-templates/{couponTemplateId}/issue", 1L)
                .withApiKeyHeaders()
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        result.andExpectAll(
            status().isOk,
            jsonPath("$.traceId").exists(),
            jsonPath("$.result").value(ResultType.SUCCESS.name),
            jsonPath("$.error").value(null),
            jsonPath("$.data.userCouponId").value(issueCouponResult.userCouponId),
            jsonPath("$.data.userId").value(issueCouponResult.userId),
            jsonPath("$.data.couponTemplateId").value(issueCouponResult.couponTemplateId),
            jsonPath("$.data.couponTemplateName").value(issueCouponResult.couponTemplateName),
        )

        result.andDocument(
            "쿠폰 발급 - 성공",
            ResourceSnippetParameters.builder()
                .tag("Coupon")
                .summary("쿠폰 발급")
                .description("쿠폰 발급 API")
                .requestHeaders(apiKeyHeaderFields())
                .pathParameters(parameterWithName("couponTemplateId").description("쿠폰 템플릿 ID"))
                .requestSchema(Schema("IssueCouponRequest"))
                .requestFields(*issueCouponRequestFields().toTypedArray())
                .responseSchema(Schema("ApiResponse<IssueCouponResult>"))
                .responseFields(*issueCouponResultFields().toTypedArray())
                .build()
        )
    }

    private fun issueCouponRequestFields() = listOf(
        fieldWithPath("userId").type(JsonFieldType.STRING).description("고객사의 고유 사용자 ID"),
    )

    private fun issueCouponResultFields() = commonSuccessResponseFields(
        fieldWithPath("data.userCouponId").type(JsonFieldType.STRING).description("발급된 쿠폰의 고유 ID"),
        fieldWithPath("data.userId").type(JsonFieldType.STRING).description("고객사의 고유 사용자 ID"),
        fieldWithPath("data.couponTemplateId").type(JsonFieldType.NUMBER).description("쿠폰 템플릿 ID"),
        fieldWithPath("data.couponTemplateName").type(JsonFieldType.STRING).description("쿠폰 템플릿 이름"),
        fieldWithPath("data.issuedAt").type(JsonFieldType.STRING).description("쿠폰 발급 시간"),
        fieldWithPath("data.expiresAt").type(JsonFieldType.STRING).optional().description("쿠폰 만료 시간")
    )
}