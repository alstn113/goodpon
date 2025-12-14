package com.goodpon.partner.api.web.coupon

import com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema
import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType
import com.goodpon.partner.api.response.ResultType
import com.goodpon.partner.api.support.AbstractDocumentTest
import com.goodpon.partner.api.support.WithMockMerchant
import com.goodpon.partner.application.coupon.service.dto.AvailableUserCouponView
import com.goodpon.partner.application.coupon.service.dto.AvailableUserCouponsView
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

class GetAvailableUserCouponsDocumentTest : AbstractDocumentTest() {

    @Test
    @WithMockMerchant
    fun `사용자의 사용 가능한 쿠폰 목록 조회 - 성공`() {
        val userId = "44321b95877380a0dcbd2b18f3aa"
        val orderId = 15000
        val availableUserCouponsView = AvailableUserCouponsView(
            coupons = listOf(
                AvailableUserCouponView(
                    userCouponId = "8f3b95144877a0dcbaad2b321380",
                    couponTemplateId = 1L,
                    couponTemplateName = "테스트 쿠폰",
                    couponTemplateDescription = "테스트 쿠폰 설명",
                    discountType = CouponDiscountType.FIXED_AMOUNT,
                    discountValue = 1000,
                    maxDiscountAmount = null,
                    minOrderAmount = 10000,
                    issuedAt = LocalDateTime.now(),
                    expiresAt = LocalDateTime.now().plusDays(30),
                    limitType = CouponLimitPolicyType.ISSUE_COUNT,
                    maxIssueCount = 1000L,
                    maxRedeemCount = null,
                    isMinOrderAmountReached = true,
                )
            )
        )

        every { getAvailableUserCouponsUseCase(any()) } returns availableUserCouponsView

        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/user-coupons/available")
                .withApiKeyHeaders()
                .param("userId", userId)
                .param("orderAmount", orderId.toString())
        )

        result.andExpectAll(
            status().isOk,
            jsonPath("$.result").value(ResultType.SUCCESS.name),
            jsonPath("$.error").value(null),
            jsonPath("$.data.coupons").isArray,
        )

        result.andDocument(
            "사용자의 사용 가능한 쿠폰 목록 조회 - 성공",
            ResourceSnippetParameters.builder()
                .tag("Coupon")
                .summary("사용자의 사용 가능한 쿠폰 목록 조회")
                .description("사용자의 사용 가능한 쿠폰 목록 조회 API")
                .requestHeaders(apiKeyHeaderFields())
                .queryParameters(
                    parameterWithName("userId").description("고객사의 고유 사용자 ID"),
                    parameterWithName("orderAmount").description("주문 금액")
                )
                .responseSchema(Schema("ApiResponse<AvailableUserCouponsView>"))
                .responseFields(*getAvailableUserCouponsResultFields().toTypedArray())
                .build()
        )
    }

    private fun getAvailableUserCouponsResultFields() = commonSuccessResponseFields(
        fieldWithPath("data.coupons").type(JsonFieldType.ARRAY).description("사용자가 보유한 쿠폰 목록"),
        fieldWithPath("data.coupons[].userCouponId").type(JsonFieldType.STRING).description("사용자 쿠폰 ID"),
        fieldWithPath("data.coupons[].couponTemplateId").type(JsonFieldType.NUMBER).description("쿠폰 템플릿 ID"),
        fieldWithPath("data.coupons[].couponTemplateName").type(JsonFieldType.STRING).description("쿠폰 템플릿 이름"),
        fieldWithPath("data.coupons[].couponTemplateDescription").type(JsonFieldType.STRING).description("쿠폰 템플릿 설명"),
        fieldWithPath("data.coupons[].discountType").type(JsonFieldType.STRING).description("할인 유형"),
        fieldWithPath("data.coupons[].discountValue").type(JsonFieldType.NUMBER).description("할인 금액"),
        fieldWithPath("data.coupons[].maxDiscountAmount").optional().type(JsonFieldType.NUMBER).description("최대 할인 금액"),
        fieldWithPath("data.coupons[].minOrderAmount").type(JsonFieldType.NUMBER).description("최소 주문 금액"),
        fieldWithPath("data.coupons[].issuedAt").type(JsonFieldType.STRING).description("발급 일시"),
        fieldWithPath("data.coupons[].expiresAt").type(JsonFieldType.STRING).description("만료 일시"),
        fieldWithPath("data.coupons[].limitType").type(JsonFieldType.STRING).description("쿠폰 제한 정책 유형"),
        fieldWithPath("data.coupons[].maxIssueCount").optional().type(JsonFieldType.NUMBER).description("최대 발급 수량"),
        fieldWithPath("data.coupons[].maxRedeemCount").optional().type(JsonFieldType.NUMBER).description("최대 사용 수량"),
        fieldWithPath("data.coupons[].isMinOrderAmountReached").type(JsonFieldType.BOOLEAN)
            .description("최소 주문 금액 도달 여부"),
    )
}