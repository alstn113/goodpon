package com.goodpon.api.core.api.controller.v1

import com.goodpon.api.core.api.controller.v1.request.CancelCouponUsageWebRequest
import com.goodpon.api.core.api.controller.v1.request.IssueCouponWebRequest
import com.goodpon.api.core.api.controller.v1.request.UseCouponWebRequest
import com.goodpon.api.core.api.response.ApiResponse
import com.goodpon.core.application.coupon.CouponIssueService
import com.goodpon.core.application.coupon.CouponUseService
import com.goodpon.core.domain.auth.MerchantPrincipal
import com.goodpon.core.domain.coupon.CouponIssueResult
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CouponController(
    private val couponIssueService: CouponIssueService,
    private val couponUseService: CouponUseService,
) {

    @PostMapping("/v1/coupons/issue")
    fun issueCoupon(
        @RequestBody request: IssueCouponWebRequest,
        @AuthenticationPrincipal merchantPrincipal: MerchantPrincipal,
    ): ResponseEntity<ApiResponse<CouponIssueResult>> {

        val appRequest = request.toAppRequest(merchantPrincipal)
        val result = couponIssueService.issueCoupon(appRequest)

        return ResponseEntity.ok(ApiResponse.of(result))
    }

    @PostMapping("/v1/coupons/{couponId}/use")
    fun useCoupon(
        @PathVariable couponId: String,
        @RequestBody request: UseCouponWebRequest,
        @AuthenticationPrincipal merchantPrincipal: MerchantPrincipal,
    ) {
        val appRequest = request.toAppRequest(merchantPrincipal, couponId)
        couponUseService.useCoupon(appRequest)
    }

    @PostMapping("/v1/coupons/{couponId}/cancel")
    fun cancelCoupon(
        @PathVariable couponId: String,
        @RequestBody request: CancelCouponUsageWebRequest,
        @AuthenticationPrincipal merchantPrincipal: MerchantPrincipal,
    ) {
        val appRequest = request.toAppRequest(merchantPrincipal, couponId)
    }
}