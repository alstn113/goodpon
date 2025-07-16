package com.goodpon.partner.openapi.controller.v1

import com.goodpon.partner.application.coupon.port.`in`.CancelCouponRedemptionUseCase
import com.goodpon.partner.application.coupon.port.`in`.IssueCouponUseCase
import com.goodpon.partner.application.coupon.port.`in`.RedeemCouponUseCase
import com.goodpon.partner.application.coupon.port.`in`.dto.CancelCouponRedemptionResult
import com.goodpon.partner.application.coupon.port.`in`.dto.IssueCouponResult
import com.goodpon.partner.application.coupon.port.`in`.dto.RedeemCouponResult
import com.goodpon.partner.openapi.controller.v1.request.CancelCouponRedemptionRequest
import com.goodpon.partner.openapi.controller.v1.request.IssueCouponRequest
import com.goodpon.partner.openapi.controller.v1.request.RedeemCouponRequest
import com.goodpon.partner.openapi.response.ApiResponse
import com.goodpon.partner.openapi.security.MerchantPrincipal
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
class CouponController(
    private val issueCouponUseCase: IssueCouponUseCase,
    private val redeemCouponUseCase: RedeemCouponUseCase,
    private val cancelCouponRedemptionUseCase: CancelCouponRedemptionUseCase,
) {

    @PostMapping("/v1/coupon-templates/{couponTemplateId}/issue")
    fun issueCoupon(
        @PathVariable couponTemplateId: Long,
        @RequestBody request: IssueCouponRequest,
        @AuthenticationPrincipal merchantPrincipal: MerchantPrincipal,
    ): ResponseEntity<ApiResponse<IssueCouponResult>> {
        val command = request.toCommand(
            merchantId = merchantPrincipal.merchantId,
            couponTemplateId = couponTemplateId
        )
        val result = issueCouponUseCase.issueCoupon(command)

        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @PostMapping("/v1/user-coupons/{userCouponId}/redeem")
    fun redeemCoupon(
        @PathVariable userCouponId: String,
        @RequestBody request: RedeemCouponRequest,
        @AuthenticationPrincipal merchantPrincipal: MerchantPrincipal,
    ): ResponseEntity<ApiResponse<RedeemCouponResult>> {
        val command = request.toCommand(merchantPrincipal.merchantId, userCouponId)
        val result = redeemCouponUseCase.redeemCoupon(command)

        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @PostMapping("/v1/user-coupons/{userCouponId}/cancel")
    fun cancelCouponRedemption(
        @PathVariable userCouponId: String,
        @RequestBody request: CancelCouponRedemptionRequest,
        @AuthenticationPrincipal merchantPrincipal: MerchantPrincipal,
    ): ResponseEntity<ApiResponse<CancelCouponRedemptionResult>> {
        val command = request.toCommand(merchantPrincipal.merchantId, userCouponId)
        val result = cancelCouponRedemptionUseCase.cancelCouponRedemption(command)

        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @GetMapping("/v1/user-coupons")
    fun getUserCoupons(
        @AuthenticationPrincipal merchantPrincipal: MerchantPrincipal,
        @RequestParam userId: String,
    ): ResponseEntity<ApiResponse<Unit>> {
        return ResponseEntity.ok(ApiResponse.success(Unit))
    }

    @GetMapping("/v1/user-coupons/available")
    fun getAvailableUserCoupons(
        @AuthenticationPrincipal merchantPrincipal: MerchantPrincipal,
        @RequestParam userId: String,
        @RequestParam orderAmount: Int,
    ): ResponseEntity<ApiResponse<Unit>> {
        return ResponseEntity.ok(ApiResponse.success(Unit))
    }

    @GetMapping("/v1/coupon-templates/{couponTemplateId}")
    fun getCouponTemplate(
        @PathVariable couponTemplateId: Long,
        @AuthenticationPrincipal merchantPrincipal: MerchantPrincipal,
        @RequestParam(required = false) userId: String? = null,
    ): ResponseEntity<ApiResponse<Unit>> {
        return ResponseEntity.ok(ApiResponse.success(Unit))
    }
}