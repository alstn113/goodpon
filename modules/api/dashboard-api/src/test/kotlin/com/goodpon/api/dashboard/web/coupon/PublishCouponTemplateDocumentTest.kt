package com.goodpon.api.dashboard.web.coupon

import com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema
import com.goodpon.api.dashboard.response.ResultType
import com.goodpon.api.dashboard.support.AbstractDocumentTest
import com.goodpon.api.dashboard.support.WithMockAccount
import com.goodpon.application.dashboard.coupon.port.`in`.dto.PublishCouponTemplateResult
import com.goodpon.application.dashboard.coupon.port.out.exception.CouponTemplateNotFoundException
import com.goodpon.application.dashboard.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.application.dashboard.coupon.service.exception.NoMerchantAccessPermissionException
import com.goodpon.application.dashboard.merchant.port.out.exception.MerchantNotFoundException
import com.goodpon.domain.coupon.template.exception.CouponTemplateInvalidStatusToPublishException
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class PublishCouponTemplateDocumentTest : AbstractDocumentTest() {

    @Test
    @WithMockAccount
    fun `쿠폰 템플릿 발행 - 성공`() {
        // given
        val publishCouponTemplateResult = PublishCouponTemplateResult(
            id = 1L,
            name = "테스트 쿠폰",
            merchantId = 1L,
            status = CouponTemplateStatus.ISSUABLE
        )

        every { publishCouponTemplateUseCase(any()) } returns publishCouponTemplateResult

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/merchants/{merchantId}/coupon-templates/{couponTemplateId}/publish", 1L, 1L)
                .withAuthHeader()
                .contentType(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpectAll(
            status().isOk,
            jsonPath("$.result").value(ResultType.SUCCESS.name),
            jsonPath("$.error").value(null),
            jsonPath("$.data.id").value(1L),
            jsonPath("$.data.name").value("테스트 쿠폰"),
            jsonPath("$.data.merchantId").value(1L),
            jsonPath("$.data.status").value("ISSUABLE")
        )

        // document
        result.andDocument(
            "쿠폰 템플릿 발행 - 성공",
            ResourceSnippetParameters.builder()
                .tag("Coupon Template")
                .summary("쿠폰 템플릿 발행")
                .description("쿠폰 템플릿 발행 API")
                .requestHeaders(authHeaderFields())
                .pathParameters(
                    parameterWithName("merchantId").description("쿠폰 템플릿이 있는 상점 ID"),
                    parameterWithName("couponTemplateId").description("발행할 쿠폰 템플릿 ID")
                )
                .responseSchema(Schema("ApiResponse<PublishCouponTemplateResult>"))
                .responseFields(*publishCouponTemplateSuccessResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockAccount
    fun `쿠폰 템플릿 발행 - 실패 - 존재하지 않는 상점`() {
        // given
        every { publishCouponTemplateUseCase(any()) } throws MerchantNotFoundException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/merchants/{merchantId}/coupon-templates/{couponTemplateId}/publish", 1L, 1L)
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
            "쿠폰 템플릿 발행 - 실패 - 존재하지 않는 상점",
            ResourceSnippetParameters.builder()
                .tag("Coupon Template")
                .requestHeaders(authHeaderFields())
                .pathParameters(
                    parameterWithName("merchantId").description("쿠폰 템플릿이 있는 상점 ID"),
                    parameterWithName("couponTemplateId").description("발행할 쿠폰 템플릿 ID")
                )
                .responseSchema(Schema("ApiResponse<Unit>"))
                .responseFields(*commonFailureResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockAccount
    fun `쿠폰 템플릿 발행 - 실패 - 상점 접근 권한 없음`() {
        // given
        every { publishCouponTemplateUseCase(any()) } throws NoMerchantAccessPermissionException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/merchants/{merchantId}/coupon-templates/{couponTemplateId}/publish", 1L, 1L)
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
            "쿠폰 템플릿 발행 - 실패 - 상점 접근 권한 없음",
            ResourceSnippetParameters.builder()
                .tag("Coupon Template")
                .requestHeaders(authHeaderFields())
                .pathParameters(
                    parameterWithName("merchantId").description("쿠폰 템플릿이 있는 상점 ID"),
                    parameterWithName("couponTemplateId").description("발행할 쿠폰 템플릿 ID")
                )
                .responseSchema(Schema("ApiResponse<Unit>"))
                .responseFields(*commonFailureResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockAccount
    fun `쿠폰 템플릿 발행 - 실패 - 존재하지 않는 쿠폰 템플릿`() {
        // given
        every { publishCouponTemplateUseCase(any()) } throws CouponTemplateNotFoundException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/merchants/{merchantId}/coupon-templates/{couponTemplateId}/publish", 1L, 1L)
                .withAuthHeader()
                .contentType(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpectAll(
            status().isNotFound,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.data").value(null),
            jsonPath("$.error.code").value("COUPON_TEMPLATE_NOT_FOUND"),
            jsonPath("$.error.message").value("존재하지 않는 쿠폰 템플릿입니다."),
            jsonPath("$.error.data").value(null)
        )

        // document
        result.andDocument(
            "쿠폰 템플릿 발행 - 실패 - 존재하지 않는 쿠폰 템플릿",
            ResourceSnippetParameters.builder()
                .tag("Coupon Template")
                .requestHeaders(authHeaderFields())
                .pathParameters(
                    parameterWithName("merchantId").description("쿠폰 템플릿이 있는 상점 ID"),
                    parameterWithName("couponTemplateId").description("발행할 쿠폰 템플릿 ID")
                )
                .responseSchema(Schema("ApiResponse<Unit>"))
                .responseFields(*commonFailureResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockAccount
    fun `쿠폰 템플릿 발행 - 실패 - 상점이 소유하지 않은 쿠폰 템플릿`() {
        // given
        every { publishCouponTemplateUseCase(any()) } throws CouponTemplateNotOwnedByMerchantException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/merchants/{merchantId}/coupon-templates/{couponTemplateId}/publish", 1L, 1L)
                .withAuthHeader()
                .contentType(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpectAll(
            status().isForbidden,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.data").value(null),
            jsonPath("$.error.code").value("COUPON_TEMPLATE_NOT_OWNED_BY_MERCHANT"),
            jsonPath("$.error.message").value("상점이 소유한 쿠폰 템플릿이 아닙니다."),
            jsonPath("$.error.data").value(null)
        )

        // document
        result.andDocument(
            "쿠폰 템플릿 발행 - 실패 - 상점이 소유하지 않은 쿠폰 템플릿",
            ResourceSnippetParameters.builder()
                .tag("Coupon Template")
                .requestHeaders(authHeaderFields())
                .pathParameters(
                    parameterWithName("merchantId").description("쿠폰 템플릿이 있는 상점 ID"),
                    parameterWithName("couponTemplateId").description("발행할 쿠폰 템플릿 ID")
                )
                .responseSchema(Schema("ApiResponse<Unit>"))
                .responseFields(*commonFailureResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockAccount
    fun `쿠폰 템플릿 발행 - 실패 - 발행할 수 없는 상태`() {
        // given
        every {
            publishCouponTemplateUseCase(any())
        } throws CouponTemplateInvalidStatusToPublishException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/merchants/{merchantId}/coupon-templates/{couponTemplateId}/publish", 1L, 1L)
                .withAuthHeader()
                .contentType(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpectAll(
            status().isBadRequest,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.data").value(null),
            jsonPath("$.error.code").value("COUPON_TEMPLATE_INVALID_STATUS_TO_PUBLISH"),
            jsonPath("$.error.message").value("쿠폰 템플릿을 발행할 수 있는 상태가 아닙니다."),
            jsonPath("$.error.data").value(null)
        )

        // document
        result.andDocument(
            "쿠폰 템플릿 발행 - 실패 - 상점이 소유하지 않은 쿠폰 템플릿",
            ResourceSnippetParameters.builder()
                .tag("Coupon Template")
                .requestHeaders(authHeaderFields())
                .pathParameters(
                    parameterWithName("merchantId").description("쿠폰 템플릿이 있는 상점 ID"),
                    parameterWithName("couponTemplateId").description("발행할 쿠폰 템플릿 ID")
                )
                .responseSchema(Schema("ApiResponse<Unit>"))
                .responseFields(*commonFailureResponseFields().toTypedArray())
                .build()
        )
    }

    private fun publishCouponTemplateSuccessResponseFields() = commonSuccessResponseFields(
        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("쿠폰 템플릿 ID"),
        fieldWithPath("data.name").type(JsonFieldType.STRING).description("쿠폰 템플릿 이름"),
        fieldWithPath("data.merchantId").type(JsonFieldType.NUMBER).description("상점 ID"),
        fieldWithPath("data.status").type(JsonFieldType.STRING).description("쿠폰 템플릿 상태 ISSUABLE")
    )
}