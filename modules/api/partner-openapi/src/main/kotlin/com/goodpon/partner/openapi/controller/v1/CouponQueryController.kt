package com.goodpon.partner.openapi.controller.v1

import com.goodpon.partner.application.coupon.port.`in`.GetAvailableUserCouponsUseCase
import com.goodpon.partner.application.coupon.port.`in`.GetCouponTemplateDetailForUserUseCase
import com.goodpon.partner.application.coupon.port.`in`.GetUserCouponsUseCase
import com.goodpon.partner.application.coupon.port.`in`.dto.GetAvailableUserCouponsQuery
import com.goodpon.partner.application.coupon.port.`in`.dto.GetCouponTemplateDetailForUserQuery
import com.goodpon.partner.application.coupon.service.dto.AvailableUserCouponsView
import com.goodpon.partner.application.coupon.service.dto.CouponTemplateDetailForUser
import com.goodpon.partner.application.coupon.service.dto.UserCouponsView
import com.goodpon.partner.openapi.response.ApiResponse
import com.goodpon.partner.openapi.security.MerchantPrincipal
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class CouponQueryController(
    private val getUserCouponsUseCase: GetUserCouponsUseCase,
    private val getAvailableUserCouponsUseCase: GetAvailableUserCouponsUseCase,
    private val getCouponTemplateDetailForUserUseCase: GetCouponTemplateDetailForUserUseCase,
) {

    @GetMapping("/v1/user-coupons")
    fun getUserCoupons(
        @AuthenticationPrincipal merchantPrincipal: MerchantPrincipal,
        @RequestParam userId: String,
    ): ResponseEntity<ApiResponse<UserCouponsView>> {
        val views = getUserCouponsUseCase(merchantId = merchantPrincipal.merchantId, userId = userId)

        return ResponseEntity.ok(ApiResponse.success(views))
    }

    @GetMapping("/v1/user-coupons/available")
    fun getAvailableUserCoupons(
        @AuthenticationPrincipal merchantPrincipal: MerchantPrincipal,
        @RequestParam userId: String,
        @RequestParam orderAmount: Int,
    ): ResponseEntity<ApiResponse<AvailableUserCouponsView>> {
        val query = GetAvailableUserCouponsQuery(
            merchantId = merchantPrincipal.merchantId,
            userId = userId,
            orderAmount = orderAmount
        )
        val views = getAvailableUserCouponsUseCase(query)

        return ResponseEntity.ok(ApiResponse.success(views))
    }

    @GetMapping("/v1/coupon-templates/{couponTemplateId}")
    fun getCouponTemplateDetailForUser(
        @PathVariable couponTemplateId: Long,
        @AuthenticationPrincipal merchantPrincipal: MerchantPrincipal,
        @RequestParam(required = false) userId: String? = null,
    ): ResponseEntity<ApiResponse<CouponTemplateDetailForUser>> {
        val query = GetCouponTemplateDetailForUserQuery(
            merchantId = merchantPrincipal.merchantId,
            couponTemplateId = couponTemplateId,
            userId = userId
        )
        val detail = getCouponTemplateDetailForUserUseCase(query)

        return ResponseEntity.ok(ApiResponse.success(detail))
    }
}