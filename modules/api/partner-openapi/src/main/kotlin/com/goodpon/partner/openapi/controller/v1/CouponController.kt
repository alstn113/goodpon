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
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class CouponController(
    private val issueCouponUseCase: IssueCouponUseCase,
    private val redeemCouponUseCase: RedeemCouponUseCase,
    private val cancelCouponRedemptionUseCase: CancelCouponRedemptionUseCase,
) {

    @PostMapping("/v1/coupons/issue")
    fun issueCoupon(
        @RequestBody request: IssueCouponRequest,
        @AuthenticationPrincipal merchantPrincipal: MerchantPrincipal,
    ): ResponseEntity<ApiResponse<IssueCouponResult>> {
        val command = request.toCommand(merchantPrincipal.id)
        val result = issueCouponUseCase.issueCoupon(command)

        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @PostMapping("/v1/coupons/{couponId}/redeem")
    fun redeemCoupon(
        @PathVariable couponId: String,
        @RequestBody request: RedeemCouponRequest,
        @AuthenticationPrincipal merchantPrincipal: MerchantPrincipal,
    ): ResponseEntity<ApiResponse<RedeemCouponResult>> {
        val command = request.toCommand(merchantPrincipal.id, couponId)
        val result = redeemCouponUseCase.redeemCoupon(command)

        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @PostMapping("/v1/coupons/{couponId}/cancel")
    fun cancelCouponRedemption(
        @PathVariable couponId: String,
        @RequestBody request: CancelCouponRedemptionRequest,
        @AuthenticationPrincipal merchantPrincipal: MerchantPrincipal,
    ): ResponseEntity<ApiResponse<CancelCouponRedemptionResult>> {
        val command = request.toCommand(merchantPrincipal.id, couponId)
        val result = cancelCouponRedemptionUseCase.cancelCouponRedemption(command)

        return ResponseEntity.ok(ApiResponse.success(result))
    }
}