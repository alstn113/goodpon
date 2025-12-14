package com.goodpon.dashboard.api.web.coupon

import com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema
import com.goodpon.dashboard.api.controller.v1.coupon.dto.CouponHistorySearchRequest
import com.goodpon.dashboard.api.response.ResultType
import com.goodpon.dashboard.api.support.AbstractDocumentTest
import com.goodpon.dashboard.api.support.WithMockAccount
import com.goodpon.dashboard.application.coupon.service.dto.CouponHistoryQueryResult
import com.goodpon.dashboard.application.coupon.service.dto.CouponHistorySummary
import com.goodpon.dashboard.application.coupon.service.exception.NoMerchantAccessPermissionException
import com.goodpon.dashboard.application.merchant.port.out.exception.MerchantNotFoundException
import com.goodpon.domain.coupon.history.CouponActionType
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate
import java.time.LocalDateTime

class GetCouponHistoriesDocumentTest : AbstractDocumentTest() {

    private val couponHistorySearchRequest = CouponHistorySearchRequest(
        startDate = LocalDate.now().minusDays(1),
        endDate = LocalDate.now().plusDays(1),
        orderId = null,
        userId = null,
        couponTemplateId = null,
        nextCursor = null,
        size = 20
    )

    @Test
    @WithMockAccount
    fun `쿠폰 내역 목록 조회 - 성공`() {
        // given
        val couponHistoryQueryResult = CouponHistoryQueryResult(
            content = listOf(
                CouponHistorySummary(
                    historyId = "14bdd144f3b95ad23213804877a0dcba",
                    actionType = CouponActionType.ISSUE,
                    recordedAt = LocalDateTime.now(),
                    orderId = "your-unique-order-id",
                    reason = null,
                    userCouponId = "213804877a0dcba14bdd144f3b95ad23",
                    userId = "your-unique-user-id",
                    couponTemplateId = 1L,
                    couponTemplateName = "여름 할인 쿠폰"
                )
            ),
            size = 20,
            hasMore = true,
            nextCursor = "dd144f3b95144877a0dcbaad2b321380"
        )

        every {
            getCouponHistoriesUseCase(any(), any(), any())
        } returns couponHistoryQueryResult

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/merchants/{merchantId}/coupon-histories", 1L)
                .withAuthHeader()
                .queryParam("startDate", couponHistorySearchRequest.startDate.toString())
                .queryParam("endDate", couponHistorySearchRequest.endDate.toString())
        )

        // then
        result.andExpectAll(
            status().isOk,
            jsonPath("$.result").value(ResultType.SUCCESS.name),
            jsonPath("$.error").value(null),
            jsonPath("$.data.content[0].historyId").value("14bdd144f3b95ad23213804877a0dcba"),
            jsonPath("$.data.content[0].actionType").value(CouponActionType.ISSUE.name),
            jsonPath("$.data.content[0].orderId").value("your-unique-order-id"),
        )

        // document
        result.andDocument(
            "쿠폰 내역 목록 조회 - 성공",
            ResourceSnippetParameters.builder()
                .tag("Coupon Template")
                .summary("쿠폰 내역 목록 조회")
                .description("쿠폰 내역 목록 조회 API")
                .requestHeaders(authHeaderFields())
                .pathParameters(parameterWithName("merchantId").description("상점 ID"))
                .queryParameters(*getCouponHistoriesQueryParameters().toTypedArray())
                .responseSchema(Schema("ApiResponse<CouponHistoryQueryResult>"))
                .responseFields(*getCouponHistoriesSuccessResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockAccount
    fun `쿠폰 내역 목록 조회 - 실패 - 존재하지 않는 상점`() {
        // given
        every {
            getCouponHistoriesUseCase(any(), any(), any())
        } throws MerchantNotFoundException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/merchants/{merchantId}/coupon-histories", 1L)
                .withAuthHeader()
                .queryParam("startDate", couponHistorySearchRequest.startDate.toString())
                .queryParam("endDate", couponHistorySearchRequest.endDate.toString())
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
            "쿠폰 내역 목록 조회 - 실패 - 존재하지 않는 상점",
            ResourceSnippetParameters.builder()
                .tag("Coupon Template")
                .requestHeaders(authHeaderFields())
                .pathParameters(parameterWithName("merchantId").description("상점 ID"))
                .queryParameters(*getCouponHistoriesQueryParameters().toTypedArray())
                .responseSchema(Schema("ApiResponse<Unit>"))
                .responseFields(*commonFailureResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockAccount
    fun `쿠폰 내역 목록 조회 - 실패 - 상점 접근 권한 없음`() {
        // given
        every {
            getCouponHistoriesUseCase(any(), any(), any())
        } throws NoMerchantAccessPermissionException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/merchants/{merchantId}/coupon-histories", 1L)
                .withAuthHeader()
                .queryParam("startDate", couponHistorySearchRequest.startDate.toString())
                .queryParam("endDate", couponHistorySearchRequest.endDate.toString())
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
            "쿠폰 내역 목록 조회 - 실패 - 상점 접근 권한 없음",
            ResourceSnippetParameters.builder()
                .tag("Coupon Template")
                .requestHeaders(authHeaderFields())
                .pathParameters(parameterWithName("merchantId").description("상점 ID"))
                .queryParameters(*getCouponHistoriesQueryParameters().toTypedArray())
                .responseSchema(Schema("ApiResponse<Unit>"))
                .responseFields(*commonFailureResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockAccount
    fun `쿠폰 내역 목록 조회 - 실패 - 입력값 유효성 검사 실패`() {
        val startDate = LocalDate.now().minusDays(1)
        val endDate = startDate.minusDays(1)

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/merchants/{merchantId}/coupon-histories", 1L)
                .withAuthHeader()
                .queryParam("startDate", startDate.toString())
                .queryParam("endDate", endDate.toString())
                .queryParam("size", "200")
        )

        // then
        result.andExpectAll(
            status().isBadRequest,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.data").value(null),
            jsonPath("$.error.code").value("BAD_REQUEST"),
            jsonPath("$.error.message").value("잘못된 요청입니다."),
        )

        // document
        result.andDocument(
            "쿠폰 내역 목록 조회 - 실패 - 입력값 유효성 검사 실패",
            ResourceSnippetParameters.builder()
                .tag("Coupon Template")
                .requestHeaders(authHeaderFields())
                .pathParameters(parameterWithName("merchantId").description("상점 ID"))
                .queryParameters(*getCouponHistoriesQueryParameters().toTypedArray())
                .responseSchema(Schema("ApiResponse<Unit>"))
                .responseFields(
                    fieldWithPath("result").type(JsonFieldType.STRING).description("요청 결과 (SUCCESS/ERROR)"),
                    fieldWithPath("data").type(JsonFieldType.NULL).description("결과 데이터 (실패 시 null)"),
                    fieldWithPath("error").type(JsonFieldType.OBJECT).description("오류 정보"),
                    fieldWithPath("error.code").type(JsonFieldType.STRING).description("오류 코드"),
                    fieldWithPath("error.message").type(JsonFieldType.STRING).description("오류 메시지"),
                    fieldWithPath("error.data").type(JsonFieldType.ARRAY).optional().description("오류 데이터"),
                    fieldWithPath("error.data[].field").type(JsonFieldType.STRING).description("오류가 발생한 필드"),
                    fieldWithPath("error.data[].message").type(JsonFieldType.STRING).description("오류 메시지")
                )
                .build()
        )
    }

    private fun getCouponHistoriesQueryParameters() = listOf(
        parameterWithName("startDate").optional().description("조회 시작 날짜 (yyyy-MM-dd 형식) (선택 사항)"),
        parameterWithName("endDate").optional().description("조회 종료 날짜 (yyyy-MM-dd 형식) (선택 사항)"),
        parameterWithName("orderId").optional().description("주문 ID (선택 사항)"),
        parameterWithName("userId").optional().description("사용자 ID (선택 사항)"),
        parameterWithName("couponTemplateId").optional().description("쿠폰 템플릿 ID (선택 사항)"),
        parameterWithName("nextCursor").optional().description("다음 페이지 커서 (쿠폰 내역 ID, 선택 사항)"),
        parameterWithName("size").optional().description("페이지 크기, 기본값은 20, 최대값은 100 (선택 사항)")
    )

    private fun getCouponHistoriesSuccessResponseFields() = commonSuccessResponseFields(
        fieldWithPath("data.content[].historyId").type(JsonFieldType.STRING).description("쿠폰 내역 ID"),
        fieldWithPath("data.content[].actionType").type(JsonFieldType.STRING).description("쿠폰 액션 타입"),
        fieldWithPath("data.content[].recordedAt").type(JsonFieldType.STRING).description("쿠폰 내역 기록 시간"),
        fieldWithPath("data.content[].orderId").type(JsonFieldType.STRING).optional().description("주문 ID (선택 사항)"),
        fieldWithPath("data.content[].reason").type(JsonFieldType.STRING).optional().description("쿠폰 내역 사유 (선택 사항)"),
        fieldWithPath("data.content[].userCouponId").type(JsonFieldType.STRING).description("사용자 쿠폰 ID"),
        fieldWithPath("data.content[].userId").type(JsonFieldType.STRING).description("사용자 ID"),
        fieldWithPath("data.content[].couponTemplateId").type(JsonFieldType.NUMBER).description("쿠폰 템플릿 ID"),
        fieldWithPath("data.content[].couponTemplateName").type(JsonFieldType.STRING).description("쿠폰 템플릿 이름"),
        fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("현재 페이지의 실제 데이터 개수"),
        fieldWithPath("data.hasMore").type(JsonFieldType.BOOLEAN).description("다음 페이지가 있는지 여부"),
        fieldWithPath("data.nextCursor").type(JsonFieldType.STRING).optional().description("다음 페이지 커서 (쿠폰 내역 ID)")
    )
}