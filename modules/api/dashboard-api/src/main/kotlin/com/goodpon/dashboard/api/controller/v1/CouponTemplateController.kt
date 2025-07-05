package com.goodpon.dashboard.api.controller.v1

import com.goodpon.api.dashboard.api.controller.v1.request.CreateCouponTemplateWebRequest
import com.goodpon.api.dashboard.api.response.ApiResponse
import com.goodpon.api.dashboard.security.AccountPrincipal
import com.goodpon.domain.application.coupon.CouponTemplateService
import com.goodpon.domain.application.coupon.response.CreateCouponTemplateResponse
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