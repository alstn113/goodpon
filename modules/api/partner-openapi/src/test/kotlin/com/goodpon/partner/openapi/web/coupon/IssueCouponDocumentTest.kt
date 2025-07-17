package com.goodpon.partner.openapi.web.coupon

import com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema
import com.goodpon.domain.coupon.template.exception.CouponTemplateIssuanceLimitExceededException
import com.goodpon.domain.coupon.template.exception.CouponTemplateIssuancePeriodException
import com.goodpon.domain.coupon.template.exception.CouponTemplateStatusNotIssuableException
import com.goodpon.partner.application.coupon.port.`in`.dto.IssueCouponResult
import com.goodpon.partner.application.coupon.port.out.exception.CouponTemplateNotFoundException
import com.goodpon.partner.application.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.partner.application.coupon.service.exception.UserCouponAlreadyIssuedException
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

    private val userId = "partner-unique-user-id"
    private val request = IssueCouponRequest(userId = userId)

    @Test
    @WithMockMerchant
    fun `쿠폰 발급 - 성공`() {
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

    @Test
    @WithMockMerchant
    fun `쿠폰 발급 - 실패 - 존재하지 않는 쿠폰 템플릿`() {
        every { issueCouponUseCase(any()) } throws CouponTemplateNotFoundException()

        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/coupon-templates/{couponTemplateId}/issue", 999L)
                .withApiKeyHeaders()
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        result.andExpectAll(
            status().isNotFound,
            jsonPath("$.traceId").exists(),
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("COUPON_TEMPLATE_NOT_FOUND"),
            jsonPath("$.error.message").value("존재하지 않는 쿠폰 템플릿입니다.")
        )

        result.andDocument(
            "쿠폰 발급 - 실패 - 존재하지 않는 쿠폰 템플릿",
            ResourceSnippetParameters.builder()
                .tag("Coupon")
                .requestHeaders(apiKeyHeaderFields())
                .pathParameters(parameterWithName("couponTemplateId").description("쿠폰 템플릿 ID"))
                .requestSchema(Schema("IssueCouponRequest"))
                .requestFields(*issueCouponRequestFields().toTypedArray())
                .responseSchema(Schema("ApiResponse<Unit>"))
                .responseFields(*commonFailureResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockMerchant
    fun `쿠폰 발급 - 실패 - 상점이 소유한 쿠폰 템플릿이 아님`() {
        every { issueCouponUseCase(any()) } throws CouponTemplateNotOwnedByMerchantException()

        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/coupon-templates/{couponTemplateId}/issue", 1L)
                .withApiKeyHeaders()
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        result.andExpectAll(
            status().isForbidden,
            jsonPath("$.traceId").exists(),
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("COUPON_TEMPLATE_NOT_OWNED_BY_MERCHANT"),
            jsonPath("$.error.message").value("해당 쿠폰 템플릿은 현재 상점에서 소유하고 있지 않습니다.")
        )

        result.andDocument(
            "쿠폰 발급 - 실패 - 상점이 소유한 쿠폰 템플릿이 아님",
            ResourceSnippetParameters.builder()
                .tag("Coupon")
                .requestHeaders(apiKeyHeaderFields())
                .pathParameters(parameterWithName("couponTemplateId").description("쿠폰 템플릿 ID"))
                .requestSchema(Schema("IssueCouponRequest"))
                .requestFields(*issueCouponRequestFields().toTypedArray())
                .responseSchema(Schema("ApiResponse<Unit>"))
                .responseFields(*commonFailureResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockMerchant
    fun `쿠폰 발급 - 실패 - 사용자가 이미 발급한 쿠폰`() {
        every { issueCouponUseCase(any()) } throws UserCouponAlreadyIssuedException()

        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/coupon-templates/{couponTemplateId}/issue", 1L)
                .withApiKeyHeaders()
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        result.andExpectAll(
            status().isBadRequest,
            jsonPath("$.traceId").exists(),
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("COUPON_ALREADY_ISSUED"),
            jsonPath("$.error.message").value("사용자가 이미 발급한 쿠폰입니다.")
        )

        result.andDocument(
            "쿠폰 발급 - 실패 - 사용자가 이미 발급한 쿠폰",
            ResourceSnippetParameters.builder()
                .tag("Coupon")
                .requestHeaders(apiKeyHeaderFields())
                .pathParameters(parameterWithName("couponTemplateId").description("쿠폰 템플릿 ID"))
                .requestSchema(Schema("IssueCouponRequest"))
                .requestFields(*issueCouponRequestFields().toTypedArray())
                .responseSchema(Schema("ApiResponse<Unit>"))
                .responseFields(*commonFailureResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockMerchant
    fun `쿠폰 발급 - 실패 - 발급할 수 있는 기간이 아님`() {
        every { issueCouponUseCase(any()) } throws CouponTemplateIssuancePeriodException()

        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/coupon-templates/{couponTemplateId}/issue", 1L)
                .withApiKeyHeaders()
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        result.andExpectAll(
            status().isBadRequest,
            jsonPath("$.traceId").exists(),
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("COUPON_NOT_ISSUABLE_PERIOD"),
            jsonPath("$.error.message").value("쿠폰을 발급할 수 있는 기간이 아닙니다.")
        )

        result.andDocument(
            "쿠폰 발급 - 실패 - 발급할 수 있는 기간이 아님",
            ResourceSnippetParameters.builder()
                .tag("Coupon")
                .requestHeaders(apiKeyHeaderFields())
                .pathParameters(parameterWithName("couponTemplateId").description("쿠폰 템플릿 ID"))
                .requestSchema(Schema("IssueCouponRequest"))
                .requestFields(*issueCouponRequestFields().toTypedArray())
                .responseSchema(Schema("ApiResponse<Unit>"))
                .responseFields(*commonFailureResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockMerchant
    fun `쿠폰 발급 - 실패 - 발급할 수 있는 상태가 아님`() {
        every { issueCouponUseCase(any()) } throws CouponTemplateStatusNotIssuableException()

        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/coupon-templates/{couponTemplateId}/issue", 1L)
                .withApiKeyHeaders()
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        result.andExpectAll(
            status().isBadRequest,
            jsonPath("$.traceId").exists(),
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("COUPON_TEMPLATE_NOT_ISSUABLE"),
            jsonPath("$.error.message").value("해당 쿠폰을 발급할 수 있는 상태가 아닙니다.")
        )

        result.andDocument(
            "쿠폰 발급 - 실패 - 발급할 수 있는 상태가 아님",
            ResourceSnippetParameters.builder()
                .tag("Coupon")
                .requestHeaders(apiKeyHeaderFields())
                .pathParameters(parameterWithName("couponTemplateId").description("쿠폰 템플릿 ID"))
                .requestSchema(Schema("IssueCouponRequest"))
                .requestFields(*issueCouponRequestFields().toTypedArray())
                .responseSchema(Schema("ApiResponse<Unit>"))
                .responseFields(*commonFailureResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockMerchant
    fun `쿠폰 발급 - 실패 - 최대 발급 수량 초과`() {
        every { issueCouponUseCase(any()) } throws CouponTemplateIssuanceLimitExceededException()

        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/coupon-templates/{couponTemplateId}/issue", 1L)
                .withApiKeyHeaders()
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        result.andExpectAll(
            status().isBadRequest,
            jsonPath("$.traceId").exists(),
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("COUPON_TEMPLATE_MAX_ISSUE_COUNT_EXCEEDED"),
            jsonPath("$.error.message").value("쿠폰 템플릿의 최대 발급 수량을 초과했습니다.")
        )

        result.andDocument(
            "쿠폰 발급 - 실패 - 최대 발급 수량 초과",
            ResourceSnippetParameters.builder()
                .tag("Coupon")
                .requestHeaders(apiKeyHeaderFields())
                .pathParameters(parameterWithName("couponTemplateId").description("쿠폰 템플릿 ID"))
                .requestSchema(Schema("IssueCouponRequest"))
                .requestFields(*issueCouponRequestFields().toTypedArray())
                .responseSchema(Schema("ApiResponse<Unit>"))
                .responseFields(*commonFailureResponseFields().toTypedArray())
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