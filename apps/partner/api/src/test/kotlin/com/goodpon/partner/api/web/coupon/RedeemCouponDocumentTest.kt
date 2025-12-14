package com.goodpon.partner.api.web.coupon

import com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.epages.restdocs.apispec.Schema
import com.goodpon.domain.coupon.template.exception.CouponTemplateRedemptionConditionNotSatisfiedException
import com.goodpon.domain.coupon.template.exception.CouponTemplateRedemptionLimitExceededException
import com.goodpon.domain.coupon.template.exception.CouponTemplateStatusNotRedeemableException
import com.goodpon.domain.coupon.user.exception.UserCouponAlreadyRedeemedException
import com.goodpon.domain.coupon.user.exception.UserCouponExpiredException
import com.goodpon.domain.coupon.user.exception.UserCouponRedeemNotAllowedException
import com.goodpon.partner.api.controller.v1.request.RedeemCouponRequest
import com.goodpon.partner.api.response.ResultType
import com.goodpon.partner.api.support.AbstractDocumentTest
import com.goodpon.partner.api.support.WithMockMerchant
import com.goodpon.partner.application.coupon.port.`in`.dto.RedeemCouponResult
import com.goodpon.partner.application.coupon.port.out.exception.CouponTemplateNotFoundException
import com.goodpon.partner.application.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.partner.application.coupon.service.exception.UserCouponNotFoundException
import com.goodpon.partner.application.coupon.service.exception.UserCouponNotOwnedByUserException
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

class RedeemCouponDocumentTest : AbstractDocumentTest() {

    private val userCouponId = "8f3b95144877a0dcbaad2b321380"
    private val orderId = "a3d2bd95142487cb8f3b7a0a1380"
    private val userId = "2bd98f3b7a8a3d7c51424b0a1380"
    private val request = RedeemCouponRequest(
        userId = userId,
        orderAmount = 15000,
        orderId = orderId
    )

    @Test
    @WithMockMerchant
    fun `쿠폰 사용 - 성공`() {
        val redeemCouponResult = RedeemCouponResult(
            userCouponId = userCouponId,
            discountAmount = 2000,
            originalPrice = 15000,
            finalPrice = 13000,
            orderId = orderId,
            redeemedAt = LocalDateTime.now()
        )

        every { redeemCouponUseCase(any()) } returns redeemCouponResult

        val result = mockMvc.perform(createRedeemCouponRequestBuilder(userCouponId))

        result.andExpectAll(
            status().isOk,
            jsonPath("$.result").value(ResultType.SUCCESS.name),
            jsonPath("$.error").value(null),
            jsonPath("$.data.userCouponId").value(redeemCouponResult.userCouponId),
            jsonPath("$.data.discountAmount").value(redeemCouponResult.discountAmount),
            jsonPath("$.data.originalPrice").value(redeemCouponResult.originalPrice),
            jsonPath("$.data.finalPrice").value(redeemCouponResult.finalPrice),
            jsonPath("$.data.orderId").value(redeemCouponResult.orderId),
            jsonPath("$.data.redeemedAt").exists()
        )

        result.andDocument(
            "쿠폰 사용 - 성공",
            ResourceSnippetParameters.builder()
                .tag("Coupon")
                .summary("쿠폰 사용")
                .description("쿠폰 사용 API")
                .requestHeaders(postHeaderFields())
                .pathParameters(parameterWithName("userCouponId").description("사용할 쿠폰의 ID"))
                .requestSchema(Schema("RedeemCouponRequest"))
                .requestFields(*redeemCouponRequestFields().toTypedArray())
                .responseSchema(Schema("ApiResponse<RedeemCouponResult>"))
                .responseFields(*redeemCouponResultFields().toTypedArray())
                .responseHeaders(*postResponseHeaderFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockMerchant
    fun `쿠폰 사용 - 실패 - 존재하지 않는 사용자 쿠폰`() {
        every { redeemCouponUseCase(any()) } throws UserCouponNotFoundException()

        val result = mockMvc.perform(createRedeemCouponRequestBuilder(userCouponId))

        result.andExpectAll(
            status().isNotFound,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("USER_COUPON_NOT_FOUND"),
            jsonPath("$.error.message").value("존재하지 않는 쿠폰입니다.")
        )

        result.andDocument(
            "쿠폰 사용 - 실패 - 존재하지 않는 사용자 쿠폰",
            commonFailureSnippetParamsBuilder().build()
        )
    }

    @Test
    @WithMockMerchant
    fun `쿠폰 사용 - 실패 - 존재하지 않는 쿠폰 템플릿`() {
        every { redeemCouponUseCase(any()) } throws CouponTemplateNotFoundException()

        val result = mockMvc.perform(createRedeemCouponRequestBuilder(userCouponId))

        result.andExpectAll(
            status().isNotFound,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("COUPON_TEMPLATE_NOT_FOUND"),
            jsonPath("$.error.message").value("존재하지 않는 쿠폰 템플릿입니다.")
        )

        result.andDocument(
            "쿠폰 사용 - 실패 - 존재하지 않는 쿠폰 템플릿",
            commonFailureSnippetParamsBuilder().build()
        )
    }

    @Test
    @WithMockMerchant
    fun `쿠폰 사용 - 실패 - 상점이 소유한 쿠폰 템플릿이 아님`() {
        every { redeemCouponUseCase(any()) } throws CouponTemplateNotOwnedByMerchantException()

        val result = mockMvc.perform(createRedeemCouponRequestBuilder(userCouponId))

        result.andExpectAll(
            status().isForbidden,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("COUPON_TEMPLATE_NOT_OWNED_BY_MERCHANT"),
            jsonPath("$.error.message").value("해당 쿠폰 템플릿은 현재 상점에서 소유하고 있지 않습니다.")
        )

        result.andDocument(
            "쿠폰 사용 - 실패 - 상점이 소유한 쿠폰 템플릿이 아님",
            commonFailureSnippetParamsBuilder().build()
        )
    }

    @Test
    @WithMockMerchant
    fun `쿠폰 사용 - 실패 - 사용자가 소유한 쿠폰이 아님`() {
        every { redeemCouponUseCase(any()) } throws UserCouponNotOwnedByUserException()

        val result = mockMvc.perform(createRedeemCouponRequestBuilder(userCouponId))

        result.andExpectAll(
            status().isForbidden,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("USER_COUPON_NOT_OWNED_BY_USER"),
            jsonPath("$.error.message").value("해당 쿠폰은 사용자가 소유한 쿠폰이 아닙니다.")
        )

        result.andDocument(
            "쿠폰 사용 - 실패 - 사용자가 소유한 쿠폰이 아님",
            commonFailureSnippetParamsBuilder().build()
        )
    }

    @Test
    @WithMockMerchant
    fun `쿠폰 사용 - 실패 - 쿠폰을 사용할 수 있는 상태가 아님`() {
        every { redeemCouponUseCase(any()) } throws CouponTemplateStatusNotRedeemableException()

        val result = mockMvc.perform(createRedeemCouponRequestBuilder(userCouponId))

        result.andExpectAll(
            status().isBadRequest,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("COUPON_NOT_REDEEMABLE"),
            jsonPath("$.error.message").value("해당 쿠폰을 사용할 수 있는 상태가 아닙니다.")
        )

        result.andDocument(
            "쿠폰 사용 - 실패 - 쿠폰을 사용할 수 있는 상태가 아님",
            commonFailureSnippetParamsBuilder().build()
        )
    }

    @Test
    @WithMockMerchant
    fun `쿠폰 사용 - 실패 - 최대 사용 수량 초과`() {
        every { redeemCouponUseCase(any()) } throws CouponTemplateRedemptionLimitExceededException()

        val result = mockMvc.perform(createRedeemCouponRequestBuilder(userCouponId))

        result.andExpectAll(
            status().isBadRequest,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("COUPON_TEMPLATE_MAX_REDEEM_COUNT_EXCEEDED"),
            jsonPath("$.error.message").value("쿠폰 템플릿의 최대 사용 수량을 초과했습니다.")
        )

        result.andDocument(
            "쿠폰 사용 - 실패 - 최대 사용 수량 초과",
            commonFailureSnippetParamsBuilder().build()
        )
    }

    @Test
    @WithMockMerchant
    fun `쿠폰 사용 - 실패 - 사용 조건을 만족하지 못함`() {
        every { redeemCouponUseCase(any()) } throws CouponTemplateRedemptionConditionNotSatisfiedException()

        val result = mockMvc.perform(createRedeemCouponRequestBuilder(userCouponId))

        result.andExpectAll(
            status().isBadRequest,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("COUPON_TEMPLATE_REDEEM_CONDITION_NOT_MET"),
            jsonPath("$.error.message").value("쿠폰 사용 조건을 충족하지 못했습니다.")
        )

        result.andDocument(
            "쿠폰 사용 - 실패 - 사용 조건을 만족하지 못함",
            commonFailureSnippetParamsBuilder().build()
        )
    }

    @Test
    @WithMockMerchant
    fun `쿠폰 사용 - 실패 - 이미 사용한 쿠폰`() {
        every { redeemCouponUseCase(any()) } throws UserCouponAlreadyRedeemedException()

        val result = mockMvc.perform(createRedeemCouponRequestBuilder(userCouponId))

        result.andExpectAll(
            status().isBadRequest,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("USER_COUPON_ALREADY_REDEEMED"),
            jsonPath("$.error.message").value("해당 쿠폰은 이미 사용된 쿠폰입니다.")
        )

        result.andDocument(
            "쿠폰 사용 - 실패 - 이미 사용한 쿠폰",
            commonFailureSnippetParamsBuilder().build()
        )
    }

    @Test
    @WithMockMerchant
    fun `쿠폰 사용 - 실패 - 쿠폰이 발급된 상태가 아님`() {
        every { redeemCouponUseCase(any()) } throws UserCouponRedeemNotAllowedException()

        val result = mockMvc.perform(createRedeemCouponRequestBuilder(userCouponId))

        result.andExpectAll(
            status().isBadRequest,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("COUPON_NOT_REDEEMABLE"),
            jsonPath("$.error.message").value("해당 쿠폰을 사용할 수 있는 상태가 아닙니다.")
        )

        result.andDocument(
            "쿠폰 사용 - 실패 - 쿠폰이 발급된 상태가 아님",
            commonFailureSnippetParamsBuilder().build()
        )
    }

    @Test
    @WithMockMerchant
    fun `쿠폰 사용 - 실패 - 만료된 쿠폰`() {
        every { redeemCouponUseCase(any()) } throws UserCouponExpiredException()

        val result = mockMvc.perform(createRedeemCouponRequestBuilder(userCouponId))

        result.andExpectAll(
            status().isBadRequest,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("USER_COUPON_EXPIRED"),
            jsonPath("$.error.message").value("해당 쿠폰은 만료된 쿠폰입니다.")
        )

        result.andDocument(
            "쿠폰 사용 - 실패 - 만료된 쿠폰",
            commonFailureSnippetParamsBuilder().build()
        )
    }

    private fun createRedeemCouponRequestBuilder(userCouponId: String): MockHttpServletRequestBuilder {
        return RestDocumentationRequestBuilders
            .post("/v1/user-coupons/{userCouponId}/redeem", userCouponId)
            .withApiKeyHeaders()
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    }

    private fun commonFailureSnippetParamsBuilder(): ResourceSnippetParametersBuilder {
        return ResourceSnippetParameters.builder()
            .tag("Coupon")
            .summary("쿠폰 사용")
            .description("쿠폰 사용 API")
            .requestHeaders(postHeaderFields())
            .pathParameters(parameterWithName("userCouponId").description("사용할 쿠폰의 ID"))
            .requestSchema(Schema("RedeemCouponRequest"))
            .requestFields(*redeemCouponRequestFields().toTypedArray())
            .responseSchema(Schema("ApiResponse<Unit>"))
            .responseFields(*commonFailureResponseFields().toTypedArray())
            .responseHeaders(*postResponseHeaderFields().toTypedArray())
    }

    private fun redeemCouponRequestFields() = listOf(
        fieldWithPath("userId").type(JsonFieldType.STRING).description("고유한 사용자 ID"),
        fieldWithPath("orderAmount").type(JsonFieldType.NUMBER).description("주문 금액"),
        fieldWithPath("orderId").type(JsonFieldType.STRING).description("고유한 주문 ID")
    )

    private fun redeemCouponResultFields() = commonSuccessResponseFields(
        fieldWithPath("data.userCouponId").type(JsonFieldType.STRING).description("사용된 쿠폰의 ID"),
        fieldWithPath("data.discountAmount").type(JsonFieldType.NUMBER).description("할인 금액"),
        fieldWithPath("data.originalPrice").type(JsonFieldType.NUMBER).description("원래 가격"),
        fieldWithPath("data.finalPrice").type(JsonFieldType.NUMBER).description("할인 적용 후 최종 가격"),
        fieldWithPath("data.orderId").type(JsonFieldType.STRING).description("주문 ID"),
        fieldWithPath("data.redeemedAt").type(JsonFieldType.STRING).description("쿠폰 사용 시각")
    )
}