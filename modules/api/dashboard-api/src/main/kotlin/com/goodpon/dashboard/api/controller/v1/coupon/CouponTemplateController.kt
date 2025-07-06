package com.goodpon.dashboard.api.controller.v1.coupon

import com.goodpon.dashboard.api.controller.v1.coupon.dto.CreateCouponTemplateWebRequest
import com.goodpon.dashboard.api.response.ApiResponse
import com.goodpon.dashboard.api.security.AccountPrincipal
import com.goodpon.dashboard.application.coupon.service.CouponTemplateService
import com.goodpon.dashboard.application.coupon.service.response.CreateCouponTemplateResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@RestController
class CouponTemplateController(
    private val couponTemplateService: CouponTemplateService,
) {

    @PostMapping("/v1/merchant/{merchantId}/coupon-templates")
    fun createCouponTemplate(
        @PathVariable merchantId: Long,
        @AuthenticationPrincipal accountPrincipal: AccountPrincipal,
        @RequestBody request: CreateCouponTemplateWebRequest,
    ): ResponseEntity<ApiResponse<CreateCouponTemplateResponse>> {
        val appRequest = request.toAppRequest(merchantId, accountPrincipal.id)
        val response = couponTemplateService.createCouponTemplate(appRequest)

        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @GetMapping("/v1/merchant/{merchantId}/coupon-templates")
    fun getCouponTemplates(
        @PathVariable merchantId: Long,
        @AuthenticationPrincipal accountPrincipal: AccountPrincipal,
    ) {
    }

    @GetMapping("/v1/merchant/{merchantId}/coupon-templates/{couponTemplateId}")
    fun getCouponTemplate(
        @PathVariable merchantId: Long,
        @PathVariable couponTemplateId: Long,
        @AuthenticationPrincipal accountPrincipal: AccountPrincipal,
    ) {
    }
}