package com.goodpon.dashboard.api.web.coupon

import com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema
import com.goodpon.dashboard.api.response.ResultType
import com.goodpon.dashboard.api.support.AbstractDocumentTest
import com.goodpon.dashboard.api.support.WithMockAccount
import com.goodpon.dashboard.application.coupon.port.out.exception.CouponTemplateNotFoundException
import com.goodpon.dashboard.application.coupon.service.dto.CouponTemplateDetail
import com.goodpon.dashboard.application.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.dashboard.application.coupon.service.exception.NoMerchantAccessPermissionException
import com.goodpon.dashboard.application.merchant.port.out.exception.MerchantNotFoundException
import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate
import java.time.LocalDateTime

class GetMerchantCouponTemplateDetailDocumentTest : AbstractDocumentTest() {

    @Test
    @WithMockAccount
    fun `쿠폰 템플릿 상세 조회 - 성공`() {
        // given
        val couponTemplateDetail = CouponTemplateDetail(
            id = 1L,
            merchantId = 1L,
            name = "테스트 쿠폰",
            description = "테스트 쿠폰 설명",
            minOrderAmount = 10000,
            discountType = CouponDiscountType.FIXED_AMOUNT,
            discountValue = 1000,
            maxDiscountAmount = null,
            status = CouponTemplateStatus.ISSUABLE,
            issueStartAt = LocalDate.of(2025, 7, 13).atStartOfDay(),
            issueEndAt = LocalDate.of(2025, 7, 21).atStartOfDay(),
            validityDays = null,
            absoluteExpiresAt = null,
            limitType = CouponLimitPolicyType.ISSUE_COUNT,
            maxIssueCount = 100L,
            maxRedeemCount = null,
            issueCount = 30,
            redeemCount = 10,
            createdAt = LocalDateTime.of(2025, 7, 13, 10, 0),
        )

        every {
            getMerchantCouponTemplateDetailUseCase(any())
        } returns couponTemplateDetail

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/merchants/{merchantId}/coupon-templates/{couponTemplateId}", 1L, 1L)
                .withAuthHeader()
        )

        // then
        result.andExpectAll(
            status().isOk,
            jsonPath("$.result").value(ResultType.SUCCESS.name),
            jsonPath("$.error").value(null),
            jsonPath("$.data.id").value(1L),
            jsonPath("$.data.name").value("테스트 쿠폰"),
            jsonPath("$.data.description").value("테스트 쿠폰 설명"),
            jsonPath("$.data.merchantId").value(1L),
        )

        // document
        result.andDocument(
            "쿠폰 템플릿 상세 조회 - 성공",
            ResourceSnippetParameters.builder()
                .tag("Coupon Template")
                .summary("쿠폰 템플릿 상세 조회")
                .description("쿠폰 템플릿 상세 조회 API")
                .requestHeaders(authHeaderFields())
                .pathParameters(
                    parameterWithName("merchantId").description("쿠폰 템플릿이 있는 상점 ID"),
                    parameterWithName("couponTemplateId").description("조회할 쿠폰 템플릿 ID")
                )
                .responseSchema(Schema("ApiResponse<CouponTemplateDetail>"))
                .responseFields(*getMerchantCouponTemplateDetailSuccessResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockAccount
    fun `쿠폰 템플릿 상세 조회 - 실패 - 존재하지 않는 상점`() {
        // given
        every {
            getMerchantCouponTemplateDetailUseCase(any())
        } throws MerchantNotFoundException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/merchants/{merchantId}/coupon-templates/{couponTemplateId}", 1L, 1L)
                .withAuthHeader()
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
            "쿠폰 템플릿 상세 조회 - 실패 - 존재하지 않는 상점",
            ResourceSnippetParameters.builder()
                .tag("Coupon Template")
                .requestHeaders(authHeaderFields())
                .pathParameters(
                    parameterWithName("merchantId").description("쿠폰 템플릿이 있는 상점 ID"),
                    parameterWithName("couponTemplateId").description("조회할 쿠폰 템플릿 ID")
                )
                .responseFields(*commonFailureResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockAccount
    fun `쿠폰 템플릿 상세 조회 - 실패 - 상점 접근 권한 없음`() {
        // given
        every {
            getMerchantCouponTemplateDetailUseCase(any())
        } throws NoMerchantAccessPermissionException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/merchants/{merchantId}/coupon-templates/{couponTemplateId}", 1L, 1L)
                .withAuthHeader()
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
            "쿠폰 템플릿 상세 조회 - 실패 - 상점 접근 권한 없음",
            ResourceSnippetParameters.builder()
                .tag("Coupon Template")
                .requestHeaders(authHeaderFields())
                .pathParameters(
                    parameterWithName("merchantId").description("쿠폰 템플릿이 있는 상점 ID"),
                    parameterWithName("couponTemplateId").description("조회할 쿠폰 템플릿 ID")
                )
                .responseFields(*commonFailureResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockAccount
    fun `쿠폰 템플릿 상세 조회 - 실패 - 존재하지 않는 쿠폰 템플릿`() {
        // given
        every {
            getMerchantCouponTemplateDetailUseCase(any())
        } throws CouponTemplateNotFoundException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/merchants/{merchantId}/coupon-templates/{couponTemplateId}", 1L, 1L)
                .withAuthHeader()
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
            "쿠폰 템플릿 상세 조회 - 실패 - 존재하지 않는 쿠폰 템플릿",
            ResourceSnippetParameters.builder()
                .tag("Coupon Template")
                .requestHeaders(authHeaderFields())
                .pathParameters(
                    parameterWithName("merchantId").description("쿠폰 템플릿이 있는 상점 ID"),
                    parameterWithName("couponTemplateId").description("조회할 쿠폰 템플릿 ID")
                )
                .responseFields(*commonFailureResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockAccount
    fun `쿠폰 템플릿 상세 조회 - 실패 - 상점이 소유하지 않은 쿠폰 템플릿`() {
        // given
        every {
            getMerchantCouponTemplateDetailUseCase(any())
        } throws CouponTemplateNotOwnedByMerchantException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/merchants/{merchantId}/coupon-templates/{couponTemplateId}", 1L, 1L)
                .withAuthHeader()
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
            "쿠폰 템플릿 상세 조회 - 실패 - 상점이 소유하지 않은 쿠폰 템플릿",
            ResourceSnippetParameters.builder()
                .tag("Coupon Template")
                .requestHeaders(authHeaderFields())
                .pathParameters(
                    parameterWithName("merchantId").description("쿠폰 템플릿이 있는 상점 ID"),
                    parameterWithName("couponTemplateId").description("조회할 쿠폰 템플릿 ID")
                )
                .responseFields(*commonFailureResponseFields().toTypedArray())
                .build()
        )
    }

    private fun getMerchantCouponTemplateDetailSuccessResponseFields() = commonSuccessResponseFields(
        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("쿠폰 템플릿 ID"),
        fieldWithPath("data.name").type(JsonFieldType.STRING).description("쿠폰 템플릿 이름"),
        fieldWithPath("data.description").type(JsonFieldType.STRING).description("쿠폰 템플릿 설명"),
        fieldWithPath("data.merchantId").type(JsonFieldType.NUMBER).description("상점 ID"),
        fieldWithPath("data.minOrderAmount").type(JsonFieldType.NUMBER).optional()
            .description("최소 주문 금액 (선택 사항)"),
        fieldWithPath("data.discountType").type(JsonFieldType.STRING)
            .description("할인 유형 (FIXED_AMOUNT, PERCENTAGE)"),
        fieldWithPath("data.discountValue").type(JsonFieldType.NUMBER).description("할인 금액 또는 비율"),
        fieldWithPath("data.maxDiscountAmount").type(JsonFieldType.NUMBER).optional()
            .description("최대 할인 금액 (PERCENTAGE 선택 시)"),
        fieldWithPath("data.status").type(JsonFieldType.STRING).description("쿠폰 템플릿 상태"),
        fieldWithPath("data.issueStartAt").type(JsonFieldType.STRING)
            .description("쿠폰 발급 시작 날짜"),
        fieldWithPath("data.issueEndAt").type(JsonFieldType.STRING).optional()
            .description("쿠폰 발급 종료 날짜"),
        fieldWithPath("data.validityDays").type(JsonFieldType.NUMBER).optional()
            .description("쿠폰 유효 기간"),
        fieldWithPath("data.absoluteExpiresAt").type(JsonFieldType.STRING).optional()
            .description("쿠폰 절대 만료 날짜"),
        fieldWithPath("data.limitType").type(JsonFieldType.STRING)
            .description("쿠폰 제한 유형"),
        fieldWithPath("data.maxIssueCount").type(JsonFieldType.NUMBER).optional()
            .description("최대 발급 수량"),
        fieldWithPath("data.maxRedeemCount").type(JsonFieldType.NUMBER).optional()
            .description("최대 사용 수량"),
        fieldWithPath("data.issueCount").type(JsonFieldType.NUMBER).description("발급된 쿠폰 수"),
        fieldWithPath("data.redeemCount").type(JsonFieldType.NUMBER).description("사용된 쿠폰 수"),
        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("쿠폰 템플릿 생성 일시")
    )
}