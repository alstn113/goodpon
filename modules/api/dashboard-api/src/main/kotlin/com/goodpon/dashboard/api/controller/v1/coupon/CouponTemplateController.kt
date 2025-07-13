package com.goodpon.dashboard.api.controller.v1.coupon

import com.goodpon.dashboard.api.controller.v1.coupon.dto.CreateCouponTemplateRequest
import com.goodpon.dashboard.api.response.ApiResponse
import com.goodpon.dashboard.api.security.AccountPrincipal
import com.goodpon.dashboard.application.coupon.port.`in`.CreateCouponTemplateUseCase
import com.goodpon.dashboard.application.coupon.port.`in`.PublishCouponTemplateUseCase
import com.goodpon.dashboard.application.coupon.port.`in`.dto.CreateCouponTemplateResult
import com.goodpon.dashboard.application.coupon.port.`in`.dto.PublishCouponTemplateCommand
import com.goodpon.dashboard.application.coupon.port.`in`.dto.PublishCouponTemplateResult
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
class CouponTemplateController(
    private val createCouponTemplateUseCase: CreateCouponTemplateUseCase,
    private val publishCouponTemplateUseCase: PublishCouponTemplateUseCase,
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
    fun getCouponTemplates(
        @PathVariable merchantId: Long,
        @AuthenticationPrincipal accountPrincipal: AccountPrincipal,
    ) {
    }

    @GetMapping("/v1/merchants/{merchantId}/coupon-templates/{couponTemplateId}")
    fun getCouponTemplate(
        @PathVariable merchantId: Long,
        @PathVariable couponTemplateId: Long,
        @AuthenticationPrincipal accountPrincipal: AccountPrincipal,
    ) {
    }
}