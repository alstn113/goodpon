package com.goodpon.dashboard.api.controller.v1.coupon

import com.goodpon.dashboard.api.controller.v1.coupon.dto.CreateCouponTemplateRequest
import com.goodpon.dashboard.api.response.ApiResponse
import com.goodpon.dashboard.api.security.AccountPrincipal
import com.goodpon.dashboard.application.coupon.port.`in`.CreateCouponTemplateUseCase
import com.goodpon.dashboard.application.coupon.port.`in`.GetMyCouponTemplateDetailUseCase
import com.goodpon.dashboard.application.coupon.port.`in`.GetMyCouponTemplatesUseCase
import com.goodpon.dashboard.application.coupon.port.`in`.PublishCouponTemplateUseCase
import com.goodpon.dashboard.application.coupon.port.`in`.dto.CreateCouponTemplateResult
import com.goodpon.dashboard.application.coupon.port.`in`.dto.GetMerchantCouponTemplateDetail
import com.goodpon.dashboard.application.coupon.port.`in`.dto.PublishCouponTemplateCommand
import com.goodpon.dashboard.application.coupon.port.`in`.dto.PublishCouponTemplateResult
import com.goodpon.dashboard.application.coupon.service.dto.CouponTemplateDetail
import com.goodpon.dashboard.application.coupon.service.dto.CouponTemplateSummary
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
class CouponTemplateController(
    private val createCouponTemplateUseCase: CreateCouponTemplateUseCase,
    private val publishCouponTemplateUseCase: PublishCouponTemplateUseCase,
    private val getMerchantCouponTemplates: GetMyCouponTemplatesUseCase,
    private val getMerchantCouponTemplateDetail: GetMyCouponTemplateDetailUseCase,
) {

    @PostMapping("/v1/merchants/{merchantId}/coupon-templates")
    fun createCouponTemplate(
        @PathVariable merchantId: Long,
        @AuthenticationPrincipal accountPrincipal: AccountPrincipal,
        @RequestBody request: CreateCouponTemplateRequest,
    ): ResponseEntity<ApiResponse<CreateCouponTemplateResult>> {
        val command = request.toCommand(merchantId, accountPrincipal.id)
        val result = createCouponTemplateUseCase.createCouponTemplate(command)

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
        val result = publishCouponTemplateUseCase.publishCouponTemplate(command)

        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @GetMapping("/v1/merchants/{merchantId}/coupon-templates")
    fun getMerchantCouponTemplates(
        @PathVariable merchantId: Long,
        @AuthenticationPrincipal accountPrincipal: AccountPrincipal,
    ): ResponseEntity<ApiResponse<List<CouponTemplateSummary>>> {
        val result = getMerchantCouponTemplates.getMerchantCouponTemplates(
            accountId = accountPrincipal.id,
            merchantId = merchantId,
        )

        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @GetMapping("/v1/merchants/{merchantId}/coupon-templates/{couponTemplateId}")
    fun getMerchantCouponTemplateDetail(
        @PathVariable merchantId: Long,
        @PathVariable couponTemplateId: Long,
        @AuthenticationPrincipal accountPrincipal: AccountPrincipal,
    ): ResponseEntity<ApiResponse<CouponTemplateDetail>> {
        val query = GetMerchantCouponTemplateDetail(
            accountId = accountPrincipal.id,
            merchantId = merchantId,
            couponTemplateId = couponTemplateId,
        )
        val result = getMerchantCouponTemplateDetail.getMerchantCouponTemplateDetail(query)

        return ResponseEntity.ok(ApiResponse.success(result))
    }
}