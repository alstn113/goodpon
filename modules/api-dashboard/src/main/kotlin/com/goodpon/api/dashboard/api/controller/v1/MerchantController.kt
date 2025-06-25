package com.goodpon.api.dashboard.api.controller.v1

import com.goodpon.core.application.merchant.MerchantService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/merchants")
class MerchantController(
    private val merchantService: MerchantService,
) {
//
//    @PostMapping
//    fun createMerchant(
//        @RequestBody request: CreateMerchantWebRequest,
//        @AuthenticationPrincipal accountPrincipal: AccountPrincipal,
//    ) {
//        val appRequest = request.toAppRequest(accountPrincipal)
//        merchantService.createMerchant(appRequest)
//    }
}
