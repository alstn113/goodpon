package com.goodpon.partner.api.web.coupon

import com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.epages.restdocs.apispec.Schema
import com.goodpon.domain.coupon.user.UserCouponStatus
import com.goodpon.partner.api.controller.v1.request.CancelCouponRedemptionRequest
import com.goodpon.partner.api.response.ResultType
import com.goodpon.partner.api.support.AbstractDocumentTest
import com.goodpon.partner.api.support.WithMockMerchant
import com.goodpon.partner.application.coupon.port.`in`.dto.CancelCouponRedemptionResult
import com.goodpon.partner.application.coupon.port.out.exception.CouponTemplateNotFoundException
import com.goodpon.partner.application.coupon.service.exception.*
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

class CancelCouponRedemptionDocumentTest : AbstractDocumentTest() {

    private val userCouponId = "8f3b95144877a0dcbaad2b321380"
    private val orderId = "b321838044d2aa8f3b95177a0dcb"
    private val request = CancelCouponRedemptionRequest(cancelReason = "결제 취소", orderId = orderId)

    @Test
    @WithMockMerchant
    fun `쿠폰 사용 취소 - 성공`() {
        val cancelCouponRedemptionResult = CancelCouponRedemptionResult(
            userCouponId = userCouponId,
            status = UserCouponStatus.ISSUED,
            canceledAt = LocalDateTime.now(),
            cancelReason = "결제 취소"
        )

        every { cancelCouponRedemptionUseCase(any()) } returns cancelCouponRedemptionResult

        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/user-coupons/{userCouponId}/cancel", userCouponId)
                .withApiKeyHeaders()
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        result.andExpectAll(
            status().isOk,
            jsonPath("$.result").value(ResultType.SUCCESS.name),
            jsonPath("$.error").value(null),
            jsonPath("$.data.userCouponId").value(userCouponId),
            jsonPath("$.data.status").value(UserCouponStatus.ISSUED.name),
            jsonPath("$.data.canceledAt").exists(),
            jsonPath("$.data.cancelReason").value("결제 취소")
        )

        result.andDocument(
            "쿠폰 사용 취소 - 성공",
            ResourceSnippetParameters.builder()
                .tag("Coupon")
                .summary("쿠폰 사용 취소")
                .description("쿠폰 사용 취소 API")
                .requestHeaders(postHeaderFields())
                .pathParameters(parameterWithName("userCouponId").description("사용을 취소할 쿠폰의 ID"))
                .requestSchema(Schema("CancelCouponRedemptionRequest"))
                .requestFields(*cancelCouponRedemptionRequestFields().toTypedArray())
                .responseSchema(Schema("ApiResponse<CancelCouponRedemptionResult>"))
                .responseFields(*cancelCouponRedemptionResultFields().toTypedArray())
                .responseHeaders(*postResponseHeaderFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockMerchant
    fun `쿠폰 사용 취소 - 실패 - 존재하지 않는 사용자 쿠폰`() {
        every { cancelCouponRedemptionUseCase(any()) } throws UserCouponNotFoundException()

        val result = mockMvc.perform(createCancelCouponRedemptionRequestBuilder(userCouponId))

        result.andExpectAll(
            status().isNotFound,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("USER_COUPON_NOT_FOUND"),
            jsonPath("$.error.message").value("존재하지 않는 쿠폰입니다.")
        )

        result.andDocument(
            "쿠폰 사용 취소 - 실패 - 존재하지 않는 사용자 쿠폰",
            commonFailureSnippetsParamsBuilder().build()
        )
    }

    @Test
    @WithMockMerchant
    fun `쿠폰 사용 취소 - 실패 - 존재하지 않는 쿠폰 템플릿`() {
        every { cancelCouponRedemptionUseCase(any()) } throws CouponTemplateNotFoundException()

        val result = mockMvc.perform(createCancelCouponRedemptionRequestBuilder(userCouponId))

        result.andExpectAll(
            status().isNotFound,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("COUPON_TEMPLATE_NOT_FOUND"),
            jsonPath("$.error.message").value("존재하지 않는 쿠폰 템플릿입니다.")
        )

        result.andDocument(
            "쿠폰 사용 취소 - 실패 - 존재하지 않는 쿠폰 템플릿",
            commonFailureSnippetsParamsBuilder().build()
        )
    }

    @Test
    @WithMockMerchant
    fun `쿠폰 사용 취소 - 실패 - 상점이 소유한 쿠폰 템플릿이 아님`() {
        every { cancelCouponRedemptionUseCase(any()) } throws CouponTemplateNotOwnedByMerchantException()

        val result = mockMvc.perform(createCancelCouponRedemptionRequestBuilder(userCouponId))

        result.andExpectAll(
            status().isForbidden,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("COUPON_TEMPLATE_NOT_OWNED_BY_MERCHANT"),
            jsonPath("$.error.message").value("해당 쿠폰 템플릿은 현재 상점에서 소유하고 있지 않습니다.")
        )

        result.andDocument(
            "쿠폰 사용 취소 - 실패 - 상점이 소유한 쿠폰 템플릿이 아님",
            commonFailureSnippetsParamsBuilder().build()
        )
    }

    @Test
    @WithMockMerchant
    fun `쿠폰 사용 취소 - 실패 - 이미 취소된 쿠폰`() {
        every { cancelCouponRedemptionUseCase(any()) } throws UserCouponAlreadyCanceledException()

        val result = mockMvc.perform(createCancelCouponRedemptionRequestBuilder(userCouponId))

        result.andExpectAll(
            status().isBadRequest,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("USER_COUPON_ALREADY_CANCELLED"),
            jsonPath("$.error.message").value("해당 쿠폰은 이미 취소된 쿠폰입니다.")
        )

        result.andDocument(
            "쿠폰 사용 취소 - 실패 - 이미 취소된 쿠폰",
            commonFailureSnippetsParamsBuilder().build()
        )
    }

    @Test
    @WithMockMerchant
    fun `쿠폰 사용 취소 - 실패 - 사용 취소할 수 없는 상태`() {
        every { cancelCouponRedemptionUseCase(any()) } throws UserCouponCancelRedemptionNotAllowedException()

        val result = mockMvc.perform(createCancelCouponRedemptionRequestBuilder(userCouponId))

        result.andExpectAll(
            status().isBadRequest,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("USER_COUPON_CANCEL_NOT_ALLOWED"),
            jsonPath("$.error.message").value("해당 쿠폰은 사용 취소할 수 없는 상태입니다.")
        )

        result.andDocument(
            "쿠폰 사용 취소 - 실패 - 사용 취소할 수 없는 상태",
            commonFailureSnippetsParamsBuilder().build()
        )
    }

    @Test
    @WithMockMerchant
    fun `쿠폰 사용 취소 - 실패 - 쿠폰을 사용했던 주문과 일치하지 않음`() {
        every { cancelCouponRedemptionUseCase(any()) } throws CouponOrderIdMismatchException()

        val result = mockMvc.perform(createCancelCouponRedemptionRequestBuilder(userCouponId))

        result.andExpectAll(
            status().isBadRequest,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.error.code").value("USER_COUPON_CANCEL_ORDER_ID_MISMATCH"),
            jsonPath("$.error.message").value("쿠폰 사용 취소 시 쿠폰을 사용했던 주문 ID와 동일해야 합니다.")
        )

        result.andDocument(
            "쿠폰 사용 취소 - 실패 - 쿠폰을 사용했던 주문과 일치하지 않음",
            commonFailureSnippetsParamsBuilder().build()
        )
    }

    private fun createCancelCouponRedemptionRequestBuilder(userCouponId: String): MockHttpServletRequestBuilder {
        return RestDocumentationRequestBuilders
            .post("/v1/user-coupons/{userCouponId}/cancel", userCouponId)
            .withApiKeyHeaders()
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    }

    private fun commonFailureSnippetsParamsBuilder(): ResourceSnippetParametersBuilder {
        return ResourceSnippetParameters.builder()
            .tag("Coupon")
            .requestHeaders(postHeaderFields())
            .pathParameters(parameterWithName("userCouponId").description("사용을 취소할 쿠폰의 ID"))
            .requestSchema(Schema("CancelCouponRedemptionRequest"))
            .requestFields(*cancelCouponRedemptionRequestFields().toTypedArray())
            .responseSchema(Schema("ApiResponse<Unit>"))
            .responseFields(*commonFailureResponseFields().toTypedArray())
            .responseHeaders(*postResponseHeaderFields().toTypedArray())
    }

    private fun cancelCouponRedemptionRequestFields() = listOf(
        fieldWithPath("cancelReason").type(JsonFieldType.STRING).description("쿠폰 사용 취소 사유"),
        fieldWithPath("orderId").type(JsonFieldType.STRING).description("고유한 주문 ID")
    )

    private fun cancelCouponRedemptionResultFields() = commonSuccessResponseFields(
        fieldWithPath("data.userCouponId").type(JsonFieldType.STRING).description("사용 취소된 쿠폰의 ID"),
        fieldWithPath("data.status").type(JsonFieldType.STRING).description("쿠폰의 현재 상태"),
        fieldWithPath("data.canceledAt").type(JsonFieldType.STRING).description("쿠폰 사용 취소 시각"),
        fieldWithPath("data.cancelReason").type(JsonFieldType.STRING).description("쿠폰 사용 취소 사유")
    )
}