package com.goodpon.api.partner.web.coupon

import com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.epages.restdocs.apispec.Schema
import com.goodpon.api.partner.controller.v1.request.IssueCouponRequest
import com.goodpon.api.partner.response.ResultType
import com.goodpon.api.partner.support.AbstractDocumentTest
import com.goodpon.api.partner.support.WithMockMerchant
import com.goodpon.application.partner.coupon.port.out.exception.CouponTemplateNotFoundException
import com.goodpon.application.partner.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.application.partner.coupon.service.exception.UserCouponAlreadyIssuedException
import com.goodpon.domain.coupon.template.exception.CouponTemplateIssuanceLimitExceededException
import com.goodpon.domain.coupon.template.exception.CouponTemplateIssuancePeriodException
import com.goodpon.domain.coupon.template.exception.CouponTemplateStatusNotIssuableException
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class IssueCouponDocumentTest : AbstractDocumentTest() {

    private val userId = "partner-unique-user-id"
    private val request = IssueCouponRequest(userId = userId)

    @Test
    @WithMockMerchant
    fun `쿠폰 발급 - 성공`() {
        every { issueCouponUseCase(any()) } returns Unit

        val result = mockMvc.perform(createIssueCouponRequestBuilder(1L))

        result.andExpectAll(
            status().isOk,
            jsonPath("$.result").value(ResultType.SUCCESS.name),
            jsonPath("$.error").value(null),
            jsonPath("$.data").value("쿠폰이 정상적으로 발급되었습니다.")
        )

        result.andDocument(
            "쿠폰 발급 - 성공",
            ResourceSnippetParameters.builder()
                .tag("Coupon")
                .summary("쿠폰 발급")
                .description("쿠폰 발급 API")
                .requestHeaders(postHeaderFields())
                .pathParameters(parameterWithName("couponTemplateId").description("쿠폰 템플릿 ID"))
                .requestSchema(Schema("IssueCouponRequest"))
                .requestFields(*issueCouponRequestFields().toTypedArray())
                .responseSchema(Schema("ApiResponse<String>"))
                .responseFields(*issueCouponResultFields().toTypedArray())
                .responseHeaders(*postResponseHeaderFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockMerchant
    fun `쿠폰 발급 - 실패 - 존재하지 않는 쿠폰 템플릿`() {
        every { issueCouponUseCase(any()) } throws CouponTemplateNotFoundException()

        val result = mockMvc.perform(createIssueCouponRequestBuilder(999L))

        result.andExpectAll(
            status().isNotFound,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("COUPON_TEMPLATE_NOT_FOUND"),
            jsonPath("$.error.message").value("존재하지 않는 쿠폰 템플릿입니다.")
        )

        result.andDocument(
            "쿠폰 발급 - 실패 - 존재하지 않는 쿠폰 템플릿",
            commonFailureSnippetParamsBuilder().build()
        )
    }

    @Test
    @WithMockMerchant
    fun `쿠폰 발급 - 실패 - 상점이 소유한 쿠폰 템플릿이 아님`() {
        every { issueCouponUseCase(any()) } throws CouponTemplateNotOwnedByMerchantException()

        val result = mockMvc.perform(createIssueCouponRequestBuilder(1L))

        result.andExpectAll(
            status().isForbidden,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("COUPON_TEMPLATE_NOT_OWNED_BY_MERCHANT"),
            jsonPath("$.error.message").value("해당 쿠폰 템플릿은 현재 상점에서 소유하고 있지 않습니다.")
        )

        result.andDocument(
            "쿠폰 발급 - 실패 - 상점이 소유한 쿠폰 템플릿이 아님",
            commonFailureSnippetParamsBuilder().build()
        )
    }

    @Test
    @WithMockMerchant
    fun `쿠폰 발급 - 실패 - 사용자가 이미 발급한 쿠폰`() {
        every { issueCouponUseCase(any()) } throws UserCouponAlreadyIssuedException()

        val result = mockMvc.perform(createIssueCouponRequestBuilder(1L))

        result.andExpectAll(
            status().isBadRequest,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("COUPON_ALREADY_ISSUED"),
            jsonPath("$.error.message").value("사용자가 이미 발급한 쿠폰입니다.")
        )

        result.andDocument(
            "쿠폰 발급 - 실패 - 사용자가 이미 발급한 쿠폰",
            commonFailureSnippetParamsBuilder().build()
        )
    }

    @Test
    @WithMockMerchant
    fun `쿠폰 발급 - 실패 - 발급할 수 있는 기간이 아님`() {
        every { issueCouponUseCase(any()) } throws CouponTemplateIssuancePeriodException()

        val result = mockMvc.perform(createIssueCouponRequestBuilder(1L))

        result.andExpectAll(
            status().isBadRequest,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("COUPON_NOT_ISSUABLE_PERIOD"),
            jsonPath("$.error.message").value("쿠폰을 발급할 수 있는 기간이 아닙니다.")
        )

        result.andDocument(
            "쿠폰 발급 - 실패 - 발급할 수 있는 기간이 아님",
            commonFailureSnippetParamsBuilder().build()
        )
    }

    @Test
    @WithMockMerchant
    fun `쿠폰 발급 - 실패 - 발급할 수 있는 상태가 아님`() {
        every { issueCouponUseCase(any()) } throws CouponTemplateStatusNotIssuableException()

        val result = mockMvc.perform(createIssueCouponRequestBuilder(1L))

        result.andExpectAll(
            status().isBadRequest,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("COUPON_TEMPLATE_NOT_ISSUABLE"),
            jsonPath("$.error.message").value("해당 쿠폰을 발급할 수 있는 상태가 아닙니다.")
        )

        result.andDocument(
            "쿠폰 발급 - 실패 - 발급할 수 있는 상태가 아님",
            commonFailureSnippetParamsBuilder().build()
        )
    }

    @Test
    @WithMockMerchant
    fun `쿠폰 발급 - 실패 - 최대 발급 수량 초과`() {
        every { issueCouponUseCase(any()) } throws CouponTemplateIssuanceLimitExceededException()

        val result = mockMvc.perform(createIssueCouponRequestBuilder(1L))

        result.andExpectAll(
            status().isBadRequest,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("COUPON_TEMPLATE_MAX_ISSUE_COUNT_EXCEEDED"),
            jsonPath("$.error.message").value("쿠폰 템플릿의 최대 발급 수량을 초과했습니다.")
        )

        result.andDocument(
            "쿠폰 발급 - 실패 - 최대 발급 수량 초과",
            commonFailureSnippetParamsBuilder().build()
        )
    }

    private fun createIssueCouponRequestBuilder(couponTemplateId: Long): MockHttpServletRequestBuilder {
        return RestDocumentationRequestBuilders
            .post("/v1/coupon-templates/{couponTemplateId}/issue", couponTemplateId)
            .withApiKeyHeaders()
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    }

    private fun commonFailureSnippetParamsBuilder(): ResourceSnippetParametersBuilder {
        return ResourceSnippetParameters.builder()
            .tag("Coupon")
            .requestHeaders(postHeaderFields())
            .pathParameters(parameterWithName("couponTemplateId").description("쿠폰 템플릿 ID"))
            .requestSchema(Schema("IssueCouponRequest"))
            .requestFields(*issueCouponRequestFields().toTypedArray())
            .responseSchema(Schema("ApiResponse<Unit>"))
            .responseFields(*commonFailureResponseFields().toTypedArray())
            .responseHeaders(*postResponseHeaderFields().toTypedArray())
    }

    private fun issueCouponRequestFields() = listOf(
        fieldWithPath("userId").type(JsonFieldType.STRING).description("고객사의 고유 사용자 ID"),
    )

    private fun issueCouponResultFields() = listOf(
        fieldWithPath("result").type(JsonFieldType.STRING).description("요청 결과 (SUCCESS/ERROR)"),
        fieldWithPath("error").type(JsonFieldType.NULL).description("오류 정보"),
        fieldWithPath("data").type(JsonFieldType.STRING).description("성공 메세지")
    )
}