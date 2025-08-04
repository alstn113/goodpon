package com.goodpon.api.partner.web.coupon

import com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.epages.restdocs.apispec.Schema
import com.goodpon.api.partner.response.ResultType
import com.goodpon.api.partner.support.AbstractDocumentTest
import com.goodpon.api.partner.support.WithMockMerchant
import com.goodpon.application.partner.coupon.port.`in`.dto.CouponIssuanceStatus
import com.goodpon.application.partner.coupon.port.out.exception.CouponTemplateNotFoundException
import com.goodpon.application.partner.coupon.service.dto.CouponTemplateDetailForUser
import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

class GetCouponTemplateDetailForUserDocumentTest : AbstractDocumentTest() {

    private val userId = "partner-unique-user-id"
    private val couponTemplateId = 1L

    @Test
    @WithMockMerchant
    fun `사용자에 따른 쿠폰 템플릿 상세 조회 - 성공`() {
        val detail = CouponTemplateDetailForUser(
            id = couponTemplateId,
            name = "테스트 쿠폰",
            description = "테스트 쿠폰 설명",
            discountType = CouponDiscountType.FIXED_AMOUNT,
            discountValue = 1000,
            maxDiscountAmount = null,
            minOrderAmount = 15000,
            status = CouponTemplateStatus.ISSUABLE,
            validityDays = 30,
            absoluteExpiresAt = LocalDateTime.now().plusDays(30),
            issueStartAt = LocalDateTime.now(),
            issueEndAt = LocalDateTime.now().plusDays(10),
            limitType = CouponLimitPolicyType.ISSUE_COUNT,
            maxIssueCount = 1000L,
            maxRedeemCount = null,
            currentIssueCount = 200L,
            currentRedeemCount = 100L,
            issuanceStatus = CouponIssuanceStatus.AVAILABLE
        )

        every { getCouponTemplateDetailForUserUseCase(any()) } returns detail

        val result = mockMvc.perform(createIssueCouponRequestBuilder(couponTemplateId, userId))

        result.andExpectAll(
            status().isOk,
            jsonPath("$.result").value(ResultType.SUCCESS.name),
            jsonPath("$.error").value(null),
        )

        result.andDocument(
            "사용자에 따른 쿠폰 템플릿 상세 조회 - 성공",
            ResourceSnippetParameters.builder()
                .tag("Coupon")
                .summary("사용자에 따른 쿠폰 템플릿 상세 조회")
                .description("사용자에 따른 쿠폰 템플릿 상세 조회 API")
                .requestHeaders(apiKeyHeaderFields())
                .pathParameters(parameterWithName("couponTemplateId").description("쿠폰 템플릿 ID"))
                .queryParameters(parameterWithName("userId").description("고객사의 고유 사용자 ID, 비로그인 사용자 시 미지정").optional())
                .responseSchema(Schema("ApiResponse<CouponTemplateDetailForUser>"))
                .responseFields(*getCouponTemplateDetailForUserResultFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockMerchant
    fun `사용자에 따른 쿠폰 템플릿 상세 조회 - 실패 - 존재하지 않는 쿠폰 템플릿`() {
        every { getCouponTemplateDetailForUserUseCase(any()) } throws CouponTemplateNotFoundException()

        val result = mockMvc.perform(createIssueCouponRequestBuilder(couponTemplateId, userId))

        result.andExpectAll(
            status().isNotFound,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("COUPON_TEMPLATE_NOT_FOUND"),
            jsonPath("$.error.message").value("존재하지 않는 쿠폰 템플릿입니다.")
        )

        result.andDocument(
            "사용자에 따른 쿠폰 템플릿 상세 조회 - 실패 - 존재하지 않는 쿠폰 템플릿",
            commonFailureSnippetParamsBuilder().build()
        )
    }

    private fun createIssueCouponRequestBuilder(
        couponTemplateId: Long,
        userId: String?,
    ): MockHttpServletRequestBuilder {
        return RestDocumentationRequestBuilders
            .get("/v1/coupon-templates/{couponTemplateId}", couponTemplateId)
            .withApiKeyHeaders()
            .queryParam("userId", userId)
    }

    private fun commonFailureSnippetParamsBuilder(): ResourceSnippetParametersBuilder {
        return ResourceSnippetParameters.builder()
            .tag("Coupon")
            .requestHeaders(apiKeyHeaderFields())
            .pathParameters(parameterWithName("couponTemplateId").description("쿠폰 템플릿 ID"))
            .queryParameters(parameterWithName("userId").description("고객사의 고유 사용자 ID, 비로그인 사용자 시 미지정").optional())
            .responseSchema(Schema("ApiResponse<Unit>"))
            .responseFields(*commonFailureResponseFields().toTypedArray())
    }

    private fun getCouponTemplateDetailForUserResultFields() = commonSuccessResponseFields(
        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("쿠폰 템플릿 ID"),
        fieldWithPath("data.name").type(JsonFieldType.STRING).description("쿠폰 템플릿 이름"),
        fieldWithPath("data.description").type(JsonFieldType.STRING).description("쿠폰 템플릿 설명"),
        fieldWithPath("data.discountType").type(JsonFieldType.STRING).description("할인 유형"),
        fieldWithPath("data.discountValue").type(JsonFieldType.NUMBER).description("할인 금액"),
        fieldWithPath("data.maxDiscountAmount").optional().type(JsonFieldType.NUMBER).description("최대 할인 금액"),
        fieldWithPath("data.minOrderAmount").type(JsonFieldType.NUMBER).description("최소 주문 금액"),
        fieldWithPath("data.status").type(JsonFieldType.STRING).description("쿠폰 템플릿 상태"),
        fieldWithPath("data.validityDays").type(JsonFieldType.NUMBER).description("유효 기간(일)"),
        fieldWithPath("data.absoluteExpiresAt").type(JsonFieldType.STRING).description("절대 만료 일시"),
        fieldWithPath("data.issueStartAt").type(JsonFieldType.STRING).description("발급 시작 일시"),
        fieldWithPath("data.issueEndAt").type(JsonFieldType.STRING).description("발급 종료 일시"),
        fieldWithPath("data.limitType").type(JsonFieldType.STRING).description("쿠폰 제한 정책 유형"),
        fieldWithPath("data.maxIssueCount").optional().type(JsonFieldType.NUMBER).description("최대 발급 수량"),
        fieldWithPath("data.maxRedeemCount").optional().type(JsonFieldType.NUMBER).description("최대 사용 수량"),
        fieldWithPath("data.currentIssueCount").type(JsonFieldType.NUMBER).description("현재 발급된 쿠폰 수량"),
        fieldWithPath("data.currentRedeemCount").type(JsonFieldType.NUMBER).description("현재 사용된 쿠폰 수량"),
        fieldWithPath("data.issuanceStatus").type(JsonFieldType.STRING)
            .description("쿠폰 발급 가능 여부 (AVAILABLE, PERIOD_NOT_STARTED, PERIOD_ENDED, MAX_REDEEM_COUNT_EXCEEDED, MAX_ISSUE_COUNT_EXCEEDED, ALREADY_ISSUED_BY_USER)"),
    )
}