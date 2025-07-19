package com.goodpon.dashboard.api.controller.v1.coupon

import com.goodpon.dashboard.api.controller.v1.coupon.dto.CouponHistorySearchRequest
import com.goodpon.dashboard.api.controller.v1.coupon.dto.CreateCouponTemplateRequest
import com.goodpon.dashboard.api.response.ApiResponse
import com.goodpon.dashboard.api.security.AccountPrincipal
import com.goodpon.dashboard.application.coupon.port.`in`.*
import com.goodpon.dashboard.application.coupon.port.`in`.dto.CreateCouponTemplateResult
import com.goodpon.dashboard.application.coupon.port.`in`.dto.GetMerchantCouponTemplateDetailQuery
import com.goodpon.dashboard.application.coupon.port.`in`.dto.PublishCouponTemplateCommand
import com.goodpon.dashboard.application.coupon.port.`in`.dto.PublishCouponTemplateResult
import com.goodpon.dashboard.application.coupon.service.dto.CouponHistoryQueryResult
import com.goodpon.dashboard.application.coupon.service.dto.CouponTemplateDetail
import com.goodpon.dashboard.application.coupon.service.dto.CouponTemplateSummaries
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
class CouponTemplateController(
    private val createCouponTemplateUseCase: CreateCouponTemplateUseCase,
    private val publishCouponTemplateUseCase: PublishCouponTemplateUseCase,
    private val getMerchantCouponTemplates: GetMerchantCouponTemplatesUseCase,
    private val getMerchantCouponTemplateDetail: GetMerchantCouponTemplateDetailUseCase,
    private val getCouponHistoriesUseCase: GetCouponHistoriesUseCase
) {

    @PostMapping("/v1/merchants/{merchantId}/coupon-templates")
    fun createCouponTemplate(
        @PathVariable merchantId: Long,
        @AuthenticationPrincipal accountPrincipal: AccountPrincipal,
        @RequestBody request: CreateCouponTemplateRequest,
    ): ResponseEntity<ApiResponse<CreateCouponTemplateResult>> {
        val command = request.toCommand(merchantId, accountPrincipal.id)
        val result = createCouponTemplateUseCase(command)

        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @PostMapping("/v1/merchants/{merchantId}/coupon-templates/{couponTemplateId}/publish")
    fun publishCouponTemplate(
        @PathVariable merchantId: Long,
        @PathVariable couponTemplateId: Long,
        @AuthenticationPrincipal accountPrincipal: AccountPrincipal,
    ): ResponseEntity<ApiResponse<PublishCouponTemplateResult>> {
        val command = PublishCouponTemplateCommand(
            merchantId = merchantId,
            couponTemplateId = couponTemplateId,
            accountId = accountPrincipal.id,
        )
        val result = publishCouponTemplateUseCase(command)

        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @GetMapping("/v1/merchants/{merchantId}/coupon-templates")
    fun getMerchantCouponTemplates(
        @PathVariable merchantId: Long,
        @AuthenticationPrincipal accountPrincipal: AccountPrincipal,
    ): ResponseEntity<ApiResponse<CouponTemplateSummaries>> {
        val summaries = getMerchantCouponTemplates(
            accountId = accountPrincipal.id,
            merchantId = merchantId,
        )

        return ResponseEntity.ok(ApiResponse.success(summaries))
    }

    @GetMapping("/v1/merchants/{merchantId}/coupon-templates/{couponTemplateId}")
    fun getMerchantCouponTemplateDetail(
        @PathVariable merchantId: Long,
        @PathVariable couponTemplateId: Long,
        @AuthenticationPrincipal accountPrincipal: AccountPrincipal,
    ): ResponseEntity<ApiResponse<CouponTemplateDetail>> {
        val query = GetMerchantCouponTemplateDetailQuery(
            accountId = accountPrincipal.id,
            merchantId = merchantId,
            couponTemplateId = couponTemplateId,
        )
        val detail = getMerchantCouponTemplateDetail(query)

        return ResponseEntity.ok(ApiResponse.success(detail))
    }

    @GetMapping("/v1/merchants/{merchantId}/coupon-histories")
    fun getCouponHistories(
        @PathVariable merchantId: Long,
        @AuthenticationPrincipal accountPrincipal: AccountPrincipal,
        @ModelAttribute @Valid request: CouponHistorySearchRequest,
    ): ResponseEntity<ApiResponse<CouponHistoryQueryResult>> {
        val query = request.toQuery()
        val result = getCouponHistoriesUseCase(merchantId, accountPrincipal.id, query)

        return ResponseEntity.ok(ApiResponse.success(result))
    }
}