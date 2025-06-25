package com.goodpon.api.dashboard.api.controller.v1

import com.goodpon.core.domain.auth.AccountPrincipal
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CouponTemplateController {

    @PostMapping("/v1/merchant/{merchantId}/coupon-templates")
    fun createCouponTemplate(
        @PathVariable merchantId: Long,
        @AuthenticationPrincipal accountPrincipal: AccountPrincipal,
    ) {
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