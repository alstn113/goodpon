package com.goodpon.api.partner.controller.v1

import com.goodpon.api.partner.controller.v1.request.CancelCouponRedemptionRequest
import com.goodpon.api.partner.controller.v1.request.IssueCouponRequest
import com.goodpon.api.partner.controller.v1.request.RedeemCouponRequest
import com.goodpon.api.partner.interceptor.Idempotent
import com.goodpon.api.partner.response.ApiResponse
import com.goodpon.api.partner.security.MerchantPrincipal
import com.goodpon.application.partner.coupon.port.`in`.CancelCouponRedemptionUseCase
import com.goodpon.application.partner.coupon.port.`in`.IssueCouponUseCase
import com.goodpon.application.partner.coupon.port.`in`.RedeemCouponUseCase
import com.goodpon.application.partner.coupon.port.`in`.dto.CancelCouponRedemptionResult
import com.goodpon.application.partner.coupon.port.`in`.dto.IssueCouponResult
import com.goodpon.application.partner.coupon.port.`in`.dto.RedeemCouponResult
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

    @Idempotent
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
        val result = issueCouponUseCase(command)

        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @Idempotent
    @PostMapping("/v1/user-coupons/{userCouponId}/redeem")
    fun redeemCoupon(
        @PathVariable userCouponId: String,
        @RequestBody request: RedeemCouponRequest,
        @AuthenticationPrincipal merchantPrincipal: MerchantPrincipal,
    ): ResponseEntity<ApiResponse<RedeemCouponResult>> {
        val command = request.toCommand(merchantPrincipal.merchantId, userCouponId)
        val result = redeemCouponUseCase(command)

        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @Idempotent
    @PostMapping("/v1/user-coupons/{userCouponId}/cancel")
    fun cancelCouponRedemption(
        @PathVariable userCouponId: String,
        @RequestBody request: CancelCouponRedemptionRequest,
        @AuthenticationPrincipal merchantPrincipal: MerchantPrincipal,
    ): ResponseEntity<ApiResponse<CancelCouponRedemptionResult>> {
        val command = request.toCommand(merchantPrincipal.merchantId, userCouponId)
        val result = cancelCouponRedemptionUseCase(command)

        return ResponseEntity.ok(ApiResponse.success(result))
    }
}