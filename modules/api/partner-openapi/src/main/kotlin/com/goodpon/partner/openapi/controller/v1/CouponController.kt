package com.goodpon.partner.openapi.controller.v1

import com.goodpon.partner.application.coupon.service.CouponCancelRedemptionService
import com.goodpon.partner.application.coupon.service.CouponIssueService
import com.goodpon.partner.application.coupon.service.CouponRedeemService
import com.goodpon.partner.application.coupon.service.response.CouponCancelRedemptionResultResponse
import com.goodpon.partner.application.coupon.service.response.CouponIssueResultResponse
import com.goodpon.partner.application.coupon.service.response.CouponRedemptionResultResponse
import com.goodpon.partner.openapi.controller.v1.request.CancelCouponRedemptionWebRequest
import com.goodpon.partner.openapi.controller.v1.request.IssueCouponWebRequest
import com.goodpon.partner.openapi.controller.v1.request.RedeemCouponWebRequest
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
    private val couponIssueService: CouponIssueService,
    private val couponRedeemService: CouponRedeemService,
    private val couponCancelRedemptionService: CouponCancelRedemptionService,
) {

    @PostMapping("/v1/coupons/issue")
    fun issueCoupon(
        @RequestBody request: IssueCouponWebRequest,
        @AuthenticationPrincipal merchantPrincipal: MerchantPrincipal,
    ): ResponseEntity<ApiResponse<CouponIssueResultResponse>> {
        val appRequest = request.toAppRequest(merchantPrincipal.id)
        val response = couponIssueService.issueCoupon(appRequest)

        return ResponseEntity.ok(ApiResponse.of("coupon", response))
    }

    @PostMapping("/v1/coupons/{couponId}/redeem")
    fun redeemCoupon(
        @PathVariable couponId: String,
        @RequestBody request: RedeemCouponWebRequest,
        @AuthenticationPrincipal merchantPrincipal: MerchantPrincipal,
    ): ResponseEntity<ApiResponse<CouponRedemptionResultResponse>> {
        val appRequest = request.toAppRequest(merchantPrincipal.id, couponId)
        val response = couponRedeemService.redeemCoupon(appRequest)

        return ResponseEntity.ok(ApiResponse.of("coupon", response))
    }

    @PostMapping("/v1/coupons/{couponId}/cancel")
    fun cancelCouponRedemption(
        @PathVariable couponId: String,
        @RequestBody request: CancelCouponRedemptionWebRequest,
        @AuthenticationPrincipal merchantPrincipal: MerchantPrincipal,
    ): ResponseEntity<ApiResponse<CouponCancelRedemptionResultResponse>> {
        val appRequest = request.toAppRequest(merchantPrincipal.id, couponId)
        val response = couponCancelRedemptionService.cancelCouponRedemption(appRequest)

        return ResponseEntity.ok(ApiResponse.of("coupon", response))
    }
}