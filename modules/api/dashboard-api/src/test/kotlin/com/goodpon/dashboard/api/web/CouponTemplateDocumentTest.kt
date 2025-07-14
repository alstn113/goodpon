package com.goodpon.dashboard.api.web

import com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.Schema
import com.goodpon.dashboard.api.controller.v1.coupon.dto.CreateCouponTemplateRequest
import com.goodpon.dashboard.api.response.ResultType
import com.goodpon.dashboard.api.support.AbstractDocumentTest
import com.goodpon.dashboard.api.support.WithMockAccount
import com.goodpon.dashboard.application.coupon.port.`in`.dto.CreateCouponTemplateResult
import com.goodpon.dashboard.application.coupon.port.`in`.dto.PublishCouponTemplateResult
import com.goodpon.dashboard.application.coupon.port.out.exception.CouponTemplateNotFoundException
import com.goodpon.dashboard.application.coupon.service.dto.CouponTemplateDetail
import com.goodpon.dashboard.application.coupon.service.dto.CouponTemplateSummary
import com.goodpon.dashboard.application.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.dashboard.application.coupon.service.exception.NoMerchantAccessPermissionException
import com.goodpon.dashboard.application.merchant.port.out.exception.MerchantNotFoundException
import com.goodpon.domain.coupon.template.exception.CouponTemplateInvalidStatusToPublishException
import com.goodpon.domain.coupon.template.exception.creation.CouponTemplateValidationError
import com.goodpon.domain.coupon.template.exception.creation.CouponTemplateValidationException
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

class CouponTemplateDocumentTest : AbstractDocumentTest() {

    private val createCouponTemplateRequest = CreateCouponTemplateRequest(
        name = "테스트 쿠폰",
        description = "테스트 쿠폰 설명",
        minOrderAmount = 10000,
        discountType = CouponDiscountType.FIXED_AMOUNT,
        discountValue = 1000,
        maxDiscountAmount = null,
        issueStartDate = LocalDate.of(2025, 7, 13),
        issueEndDate = LocalDate.of(2025, 7, 20),
        validityDays = 30,
        absoluteExpiryDate = LocalDate.of(2025, 8, 20),
        limitType = CouponLimitPolicyType.ISSUE_COUNT,
        maxIssueCount = 2000L,
        maxRedeemCount = null,
    )

    @Test
    @WithMockAccount
    fun `쿠폰 템플릿 생성 - 성공`() {
        // given
        val createCouponTemplateResult = CreateCouponTemplateResult(
            id = 1L,
            name = createCouponTemplateRequest.name,
            description = createCouponTemplateRequest.description,
            merchantId = 1L,
            minOrderAmount = createCouponTemplateRequest.minOrderAmount,
            discountType = createCouponTemplateRequest.discountType,
            discountValue = createCouponTemplateRequest.discountValue,
            maxDiscountAmount = createCouponTemplateRequest.maxDiscountAmount,
            issueStartAt = LocalDate.of(2025, 7, 13).atStartOfDay(),
            issueEndAt = LocalDate.of(2025, 7, 21)?.atStartOfDay(),
            validityDays = createCouponTemplateRequest.validityDays,
            absoluteExpiresAt = LocalDate.of(2025, 8, 21).atStartOfDay(),
            limitType = createCouponTemplateRequest.limitType,
            maxIssueCount = createCouponTemplateRequest.maxIssueCount,
            maxRedeemCount = createCouponTemplateRequest.maxRedeemCount,
            status = CouponTemplateStatus.DRAFT
        )

        every { createCouponTemplateUseCase.createCouponTemplate(any()) } returns createCouponTemplateResult

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/merchants/{merchantId}/coupon-templates", 1L)
                .withAuthHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCouponTemplateRequest))
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
            jsonPath("$.data.minOrderAmount").value(10000),
            jsonPath("$.data.discountType").value("FIXED_AMOUNT"),
            jsonPath("$.data.discountValue").value(1000),
            jsonPath("$.data.maxDiscountAmount").value(null)
        )

        // document
        result.andDocument(
            "쿠폰 템플릿 생성 - 성공",
            ResourceSnippetParameters.builder()
                .tag("Coupon Template")
                .summary("쿠폰 템플릿 생성")
                .description("쿠폰 템플릿 생성 API")
                .requestHeaders(authHeaderFields())
                .pathParameters(parameterWithName("merchantId").description("쿠폰 템플릿을 생성할 상점 ID"))
                .requestSchema(Schema("CreateCouponTemplateRequest"))
                .requestFields(*createCouponTemplateRequestFields().toTypedArray())
                .responseSchema(Schema("ApiResponse<CreateCouponTemplateResult>"))
                .responseFields(*createCouponTemplateSuccessResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockAccount
    fun `쿠폰 템플릿 생성 - 실패 - 존재하지 않는 상점`() {
        // given
        every { createCouponTemplateUseCase.createCouponTemplate(any()) } throws MerchantNotFoundException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/merchants/{merchantId}/coupon-templates", 1L)
                .withAuthHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCouponTemplateRequest))
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
            "쿠폰 템플릿 생성 - 실패 - 존재하지 않는 상점",
            ResourceSnippetParameters.builder()
                .tag("Coupon Template")
                .requestHeaders(authHeaderFields())
                .pathParameters(parameterWithName("merchantId").description("쿠폰 템플릿을 생성할 상점 ID"))
                .requestSchema(Schema("CreateCouponTemplateRequest"))
                .requestFields(*createCouponTemplateRequestFields().toTypedArray())
                .responseFields(*failureResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockAccount
    fun `쿠폰 템플릿 생성 - 실패 - 상점 접근 권한 없음`() {
        // given
        every { createCouponTemplateUseCase.createCouponTemplate(any()) } throws NoMerchantAccessPermissionException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/merchants/{merchantId}/coupon-templates", 1L)
                .withAuthHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCouponTemplateRequest))
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
            "쿠폰 템플릿 생성 - 실패 - 상점 접근 권한 없음",
            ResourceSnippetParameters.builder()
                .tag("Coupon Template")
                .requestHeaders(authHeaderFields())
                .pathParameters(parameterWithName("merchantId").description("쿠폰 템플릿을 생성할 상점 ID"))
                .requestSchema(Schema("CreateCouponTemplateRequest"))
                .requestFields(*createCouponTemplateRequestFields().toTypedArray())
                .responseFields(*failureResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockAccount
    fun `쿠폰 템플릿 생성 - 실패 - 유효하지 않은 요청 데이터`() {
        // given
        val invalidRequest = createCouponTemplateRequest.copy(
            minOrderAmount = 0, // 최소 주문 금액이 있을 경우 0보다 커야 함
            discountType = CouponDiscountType.PERCENTAGE,
            discountValue = 105, // 할인율은 0~100 사이여야 함
            maxDiscountAmount = 5000,
            issueStartDate = LocalDate.now().plusDays(5),
            issueEndDate = LocalDate.now().plusDays(3), // 종료일은 시작일보다 이후여야 함
            validityDays = 3,
            absoluteExpiryDate = LocalDate.now().minusDays(8),
            limitType = CouponLimitPolicyType.ISSUE_COUNT,
            maxIssueCount = 100L,
            maxRedeemCount = 200L // 발급 횟수 제한을 설정한 경우 사용 횟수 제한을 설정할 수 없음.
        )

        val errors = listOf(
            CouponTemplateValidationError(
                field = "minOrderAmount",
                rejectedValue = 0,
                message = "쿠폰 사용 조건의 최소 주문 금액은 0보다 커야 합니다."
            ),
            CouponTemplateValidationError(
                field = "discountValue",
                rejectedValue = 105,
                message = "백분율 할인 값은 1~100 사이여야 합니다."
            ),
            CouponTemplateValidationError(
                field = "issueEndDate",
                rejectedValue = LocalDate.now().plusDays(3),
                message = "쿠폰 발행 종료일은 발행 시작일보다 이전일 수 없습니다."
            ),
            CouponTemplateValidationError(
                field = "maxRedeemCount",
                rejectedValue = 200L,
                message = "발행 제한 정책이 설정된 쿠폰은 사용 제한 수량을 함께 설정할 수 없습니다."
            ),
        )

        every {
            createCouponTemplateUseCase.createCouponTemplate(any())
        } throws CouponTemplateValidationException(errors)

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/merchants/{merchantId}/coupon-templates", 1L)
                .withAuthHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest))
        )

        // then
        result.andExpectAll(
            status().isBadRequest,
            jsonPath("$.result").value(ResultType.ERROR.name),
            jsonPath("$.data").value(null),
            jsonPath("$.error.code").value("COUPON_TEMPLATE_VALIDATION_FAILED"),
            jsonPath("$.error.message").value("잘못된 쿠폰 템플릿 생성 요청입니다. 각 필드의 오류를 확인하세요."),
            jsonPath("$.error.data[0].field").value("minOrderAmount"),
            jsonPath("$.error.data[1].field").value("discountValue"),
            jsonPath("$.error.data[2].field").value("issueEndDate"),
            jsonPath("$.error.data[3].field").value("maxRedeemCount"),
        )

        // document
        result.andDocument(
            "쿠폰 템플릿 생성 - 실패 - 유효하지 않은 요청 데이터",
            ResourceSnippetParameters.builder()
                .tag("Coupon Template")
                .requestHeaders(authHeaderFields())
                .pathParameters(parameterWithName("merchantId").description("쿠폰 템플릿을 생성할 상점 ID"))
                .requestSchema(Schema("CreateCouponTemplateRequest"))
                .requestFields(*createCouponTemplateRequestFields().toTypedArray())
                .responseFields(
                    fieldWithPath("result").type(JsonFieldType.STRING).description("요청 결과 (SUCCESS/ERROR)"),
                    fieldWithPath("data").type(JsonFieldType.NULL).description("결과 데이터 (실패 시 null)"),
                    fieldWithPath("error").type(JsonFieldType.OBJECT).description("오류 정보"),
                    fieldWithPath("error.code").type(JsonFieldType.STRING).description("오류 코드"),
                    fieldWithPath("error.message").type(JsonFieldType.STRING).description("오류 메시지"),
                    fieldWithPath("error.data").type(JsonFieldType.ARRAY)
                        .description("오류 세부 정보 (필드별 오류 메시지)"),
                    fieldWithPath("error.data[].field").type(JsonFieldType.STRING)
                        .description("오류가 발생한 필드 이름 (minOrderAmount, discountValue, issueEndDate, maxIssueCount 등)"),
                    fieldWithPath("error.data[].message").type(JsonFieldType.STRING)
                        .description("해당 필드의 오류 메시지")
                )
                .build()
        )
    }

    private fun createCouponTemplateRequestFields() = listOf(
        fieldWithPath("name").type(JsonFieldType.STRING).description("쿠폰 템플릿 이름"),
        fieldWithPath("description").type(JsonFieldType.STRING).description("쿠폰 템플릿 설명"),
        fieldWithPath("minOrderAmount").type(JsonFieldType.NUMBER).optional().description("최소 주문 금액"),
        fieldWithPath("discountType").type(JsonFieldType.STRING).description("할인 유형 (FIXED_AMOUNT, PERCENTAGE)"),
        fieldWithPath("discountValue").type(JsonFieldType.NUMBER).description("할인 금액 또는 비율"),
        fieldWithPath("maxDiscountAmount").type(JsonFieldType.NUMBER).optional()
            .description("최대 할인 금액 (PERCENTAGE 선택 시)"),
        fieldWithPath("issueStartDate").type(JsonFieldType.STRING).description("쿠폰 발급 시작 날짜 (YYYY-MM-DD 형식)"),
        fieldWithPath("issueEndDate").type(JsonFieldType.STRING).optional().description("쿠폰 발급 종료 날짜 (YYYY-MM-DD 형식)"),
        fieldWithPath("validityDays").type(JsonFieldType.NUMBER).optional().description("쿠폰 유효 기간 (일 단위)"),
        fieldWithPath("absoluteExpiryDate").type(JsonFieldType.STRING).optional()
            .description("쿠폰 절대 만료 날짜 (YYYY-MM-DD 형식)"),
        fieldWithPath("limitType").type(JsonFieldType.STRING).description("쿠폰 제한 유형 (ISSUE_COUNT, REDEEM_COUNT, NONE)"),
        fieldWithPath("maxIssueCount").type(JsonFieldType.NUMBER).optional().description("최대 발급 수량 (ISSUE_COUNT 선택 시)"),
        fieldWithPath("maxRedeemCount").type(JsonFieldType.NUMBER).optional()
            .description("최대 사용 수량 (REDEEM_COUNT 선택 시)")
    )

    private fun createCouponTemplateSuccessResponseFields() = listOf(
        fieldWithPath("result").type(JsonFieldType.STRING).description("요청 결과 (SUCCESS/ERROR)"),
        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
        fieldWithPath("error").type(JsonFieldType.NULL).description("오류 정보"),
        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("쿠폰 템플릿 ID"),
        fieldWithPath("data.name").type(JsonFieldType.STRING).description("쿠폰 템플릿 이름"),
        fieldWithPath("data.description").type(JsonFieldType.STRING).description("쿠폰 템플릿 설명"),
        fieldWithPath("data.merchantId").type(JsonFieldType.NUMBER).description("상점 ID"),
        fieldWithPath("data.minOrderAmount").type(JsonFieldType.NUMBER).optional().description("최소 주문 금액"),
        fieldWithPath("data.discountType").type(JsonFieldType.STRING).description("할인 유형 (FIXED_AMOUNT, PERCENTAGE)"),
        fieldWithPath("data.discountValue").type(JsonFieldType.NUMBER).description("할인 금액 또는 비율"),
        fieldWithPath("data.maxDiscountAmount").type(JsonFieldType.NUMBER).optional()
            .description("최대 할인 금액 (PERCENTAGE 선택 시)"),
        fieldWithPath("data.issueStartAt").type(JsonFieldType.STRING).description("쿠폰 발급 시작 날짜"),
        fieldWithPath("data.issueEndAt").type(JsonFieldType.STRING).optional()
            .description("쿠폰 발급 종료 날짜"),
        fieldWithPath("data.validityDays").type(JsonFieldType.NUMBER).optional()
            .description("쿠폰 유효 기간 (일 단위)"),
        fieldWithPath("data.absoluteExpiresAt").type(JsonFieldType.STRING).optional()
            .description("쿠폰 절대 만료 날짜"),
        fieldWithPath("data.limitType").type(JsonFieldType.STRING)
            .description("쿠폰 제한 유형 (ISSUE_COUNT, REDEEM_COUNT, NONE)"),
        fieldWithPath("data.maxIssueCount").type(JsonFieldType.NUMBER).optional()
            .description("최대 발급 수량 (ISSUE_COUNT 선택 시"),
        fieldWithPath("data.maxRedeemCount").type(JsonFieldType.NUMBER).optional()
            .description("최대 사용 수량 (REDEEM_COUNT 선택 시)"),
        fieldWithPath("data.status").type(JsonFieldType.STRING).description("쿠폰 템플릿 상태 (DRAFT, ACTIVE, EXPIRED 등)")
    )

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

        every { publishCouponTemplateUseCase.publishCouponTemplate(any()) } returns publishCouponTemplateResult

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
        every { publishCouponTemplateUseCase.publishCouponTemplate(any()) } throws MerchantNotFoundException()

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
                .responseFields(*failureResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockAccount
    fun `쿠폰 템플릿 발행 - 실패 - 상점 접근 권한 없음`() {
        // given
        every { publishCouponTemplateUseCase.publishCouponTemplate(any()) } throws NoMerchantAccessPermissionException()

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
                .responseFields(*failureResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockAccount
    fun `쿠폰 템플릿 발행 - 실패 - 존재하지 않는 쿠폰 템플릿`() {
        // given
        every { publishCouponTemplateUseCase.publishCouponTemplate(any()) } throws CouponTemplateNotFoundException()

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
                .responseFields(*failureResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockAccount
    fun `쿠폰 템플릿 발행 - 실패 - 상점이 소유하지 않은 쿠폰 템플릿`() {
        // given
        every { publishCouponTemplateUseCase.publishCouponTemplate(any()) } throws CouponTemplateNotOwnedByMerchantException()

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
                .responseFields(*failureResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockAccount
    fun `쿠폰 템플릿 발행 - 실패 - 발행할 수 없는 상태`() {
        // given
        every {
            publishCouponTemplateUseCase.publishCouponTemplate(any())
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
                .responseFields(*failureResponseFields().toTypedArray())
                .build()
        )
    }

    private fun publishCouponTemplateSuccessResponseFields() = listOf(
        fieldWithPath("result").type(JsonFieldType.STRING).description("요청 결과 (SUCCESS/ERROR)"),
        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
        fieldWithPath("error").type(JsonFieldType.NULL).description("오류 정보"),
        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("쿠폰 템플릿 ID"),
        fieldWithPath("data.name").type(JsonFieldType.STRING).description("쿠폰 템플릿 이름"),
        fieldWithPath("data.merchantId").type(JsonFieldType.NUMBER).description("상점 ID"),
        fieldWithPath("data.status").type(JsonFieldType.STRING).description("쿠폰 템플릿 상태 ISSUABLE")
    )

    @Test
    @WithMockAccount
    fun `상점의 쿠폰 템플릿 목록 조회 - 성공`() {
        // given
        val couponTemplateSummaries = listOf(
            CouponTemplateSummary(
                id = 2L,
                name = "테스트 쿠폰 2",
                description = "테스트 쿠폰 설명 2",
                status = CouponTemplateStatus.ISSUABLE,
                issueCount = 100,
                redeemCount = 50,
                createdAt = LocalDateTime.of(2025, 7, 14, 10, 0),
            ),
            CouponTemplateSummary(
                id = 1L,
                name = "테스트 쿠폰 1",
                description = "테스트 쿠폰 설명 1",
                status = CouponTemplateStatus.ISSUABLE,
                issueCount = 40,
                redeemCount = 30,
                createdAt = LocalDateTime.of(2025, 7, 13, 10, 0),
            )
        )

        every {
            getMerchantCouponTemplatesUseCase.getMerchantCouponTemplates(any(), any())
        } returns couponTemplateSummaries

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/merchants/{merchantId}/coupon-templates", 1L)
                .withAuthHeader()
                .contentType(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpectAll(
            status().isOk,
            jsonPath("$.result").value(ResultType.SUCCESS.name),
            jsonPath("$.error").value(null),
            jsonPath("$.data[0].id").value(2L),
            jsonPath("$.data[0].name").value("테스트 쿠폰 2"),
            jsonPath("$.data[1].id").value(1L),
            jsonPath("$.data[1].name").value("테스트 쿠폰 1"),
        )

        // document
        result.andDocument(
            "상점의 쿠폰 템플릿 목록 조회 - 성공",
            ResourceSnippetParameters.builder()
                .tag("Coupon Template")
                .summary("상점의 쿠폰 템플릿 목록 조회")
                .description("상점의 쿠폰 템플릿 목록 조회 API")
                .requestHeaders(authHeaderFields())
                .pathParameters(
                    parameterWithName("merchantId").description("쿠폰 템플릿이 있는 상점 ID")
                )
                .responseSchema(Schema("ApiResponse<List<CouponTemplateSummary>>"))
                .responseFields(*getMerchantCouponTemplatesSuccessResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockAccount
    fun `상점의 쿠폰 템플릿 목록 조회 - 실패 - 존재하지 않는 상점`() {
        // given
        every {
            getMerchantCouponTemplatesUseCase.getMerchantCouponTemplates(any(), any())
        } throws MerchantNotFoundException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/merchants/{merchantId}/coupon-templates", 1L)
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
            "상점의 쿠폰 템플릿 목록 조회 - 실패 - 존재하지 않는 상점",
            ResourceSnippetParameters.builder()
                .tag("Coupon Template")
                .requestHeaders(authHeaderFields())
                .pathParameters(
                    parameterWithName("merchantId").description("쿠폰 템플릿이 있는 상점 ID")
                )
                .responseFields(*failureResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockAccount
    fun `상점의 쿠폰 템플릿 목록 조회 - 실패 - 상점 접근 권한 없음`() {
        // given
        every {
            getMerchantCouponTemplatesUseCase.getMerchantCouponTemplates(any(), any())
        } throws NoMerchantAccessPermissionException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/merchants/{merchantId}/coupon-templates", 1L)
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
            "상점의 쿠폰 템플릿 목록 조회 - 실패 - 상점 접근 권한 없음",
            ResourceSnippetParameters.builder()
                .tag("Coupon Template")
                .requestHeaders(authHeaderFields())
                .pathParameters(
                    parameterWithName("merchantId").description("쿠폰 템플릿이 있는 상점 ID")
                )
                .responseFields(*failureResponseFields().toTypedArray())
                .build()
        )
    }

    private fun getMerchantCouponTemplatesSuccessResponseFields() = listOf(
        fieldWithPath("result").type(JsonFieldType.STRING).description("요청 결과 (SUCCESS/ERROR)"),
        fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답 데이터"),
        fieldWithPath("error").type(JsonFieldType.NULL).description("오류 정보"),
        fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("쿠폰 템플릿 ID"),
        fieldWithPath("data[].name").type(JsonFieldType.STRING).description("쿠폰 템플릿 이름"),
        fieldWithPath("data[].description").type(JsonFieldType.STRING).description("쿠폰 템플릿 설명"),
        fieldWithPath("data[].status").type(JsonFieldType.STRING).description("쿠폰 템플릿 상태"),
        fieldWithPath("data[].issueCount").type(JsonFieldType.NUMBER).description("발급된 쿠폰 수"),
        fieldWithPath("data[].redeemCount").type(JsonFieldType.NUMBER).description("사용된 쿠폰 수"),
        fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("쿠폰 템플릿 생성 일시"),
    )

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
            getMerchantCouponTemplateDetailUseCase.getMerchantCouponTemplateDetail(any())
        } returns couponTemplateDetail

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/merchants/{merchantId}/coupon-templates/{couponTemplateId}", 1L, 1L)
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
            jsonPath("$.data.description").value("테스트 쿠폰 설명"),
            jsonPath("$.data.merchantId").value(1L),
        )

        // document
        result.andDocument(
            "상점의 쿠폰 템플릿 목록 조회 - 성공",
            ResourceSnippetParameters.builder()
                .tag("Coupon Template")
                .summary("상점의 쿠폰 템플릿 목록 조회")
                .description("상점의 쿠폰 템플릿 목록 조회 API")
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
            getMerchantCouponTemplateDetailUseCase.getMerchantCouponTemplateDetail(any())
        } throws MerchantNotFoundException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/merchants/{merchantId}/coupon-templates/{couponTemplateId}", 1L, 1L)
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
            "쿠폰 템플릿 상세 조회 - 실패 - 존재하지 않는 상점",
            ResourceSnippetParameters.builder()
                .tag("Coupon Template")
                .requestHeaders(authHeaderFields())
                .pathParameters(
                    parameterWithName("merchantId").description("쿠폰 템플릿이 있는 상점 ID"),
                    parameterWithName("couponTemplateId").description("조회할 쿠폰 템플릿 ID")
                )
                .responseFields(*failureResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockAccount
    fun `쿠폰 템플릿 상세 조회 - 실패 - 상점 접근 권한 없음`() {
        // given
        every {
            getMerchantCouponTemplateDetailUseCase.getMerchantCouponTemplateDetail(any())
        } throws NoMerchantAccessPermissionException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/merchants/{merchantId}/coupon-templates/{couponTemplateId}", 1L, 1L)
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
            "쿠폰 템플릿 상세 조회 - 실패 - 상점 접근 권한 없음",
            ResourceSnippetParameters.builder()
                .tag("Coupon Template")
                .requestHeaders(authHeaderFields())
                .pathParameters(
                    parameterWithName("merchantId").description("쿠폰 템플릿이 있는 상점 ID"),
                    parameterWithName("couponTemplateId").description("조회할 쿠폰 템플릿 ID")
                )
                .responseFields(*failureResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockAccount
    fun `쿠폰 템플릿 상세 조회 - 실패 - 존재하지 않는 쿠폰 템플릿`() {
        // given
        every {
            getMerchantCouponTemplateDetailUseCase.getMerchantCouponTemplateDetail(any())
        } throws CouponTemplateNotFoundException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/merchants/{merchantId}/coupon-templates/{couponTemplateId}", 1L, 1L)
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
            "쿠폰 템플릿 상세 조회 - 실패 - 존재하지 않는 쿠폰 템플릿",
            ResourceSnippetParameters.builder()
                .tag("Coupon Template")
                .requestHeaders(authHeaderFields())
                .pathParameters(
                    parameterWithName("merchantId").description("쿠폰 템플릿이 있는 상점 ID"),
                    parameterWithName("couponTemplateId").description("조회할 쿠폰 템플릿 ID")
                )
                .responseFields(*failureResponseFields().toTypedArray())
                .build()
        )
    }

    @Test
    @WithMockAccount
    fun `쿠폰 템플릿 상세 조회 - 실패 - 상점이 소유하지 않은 쿠폰 템플릿`() {
        // given
        every {
            getMerchantCouponTemplateDetailUseCase.getMerchantCouponTemplateDetail(any())
        } throws CouponTemplateNotOwnedByMerchantException()

        // when
        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/merchants/{merchantId}/coupon-templates/{couponTemplateId}", 1L, 1L)
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
            "쿠폰 템플릿 상세 조회 - 실패 - 상점이 소유하지 않은 쿠폰 템플릿",
            ResourceSnippetParameters.builder()
                .tag("Coupon Template")
                .requestHeaders(authHeaderFields())
                .pathParameters(
                    parameterWithName("merchantId").description("쿠폰 템플릿이 있는 상점 ID"),
                    parameterWithName("couponTemplateId").description("조회할 쿠폰 템플릿 ID")
                )
                .responseFields(*failureResponseFields().toTypedArray())
                .build()
        )
    }

    private fun getMerchantCouponTemplateDetailSuccessResponseFields() = listOf(
        fieldWithPath("result").type(JsonFieldType.STRING).description("요청 결과 (SUCCESS/ERROR)"),
        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
        fieldWithPath("error").type(JsonFieldType.NULL).description("오류 정보"),
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