package com.goodpon.api.dashboard.api.controller.v1

import com.goodpon.api.dashboard.api.controller.v1.request.CreateMerchantWebRequest
import com.goodpon.api.dashboard.api.response.ApiResponse
import com.goodpon.core.application.merchant.MerchantService
import com.goodpon.core.application.merchant.response.CreateMerchantResponse
import com.goodpon.core.domain.auth.AccountPrincipal
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/merchants")
class MerchantController(
    private val merchantService: MerchantService,
) {

    @PostMapping
    fun createMerchant(
        @RequestBody request: CreateMerchantWebRequest,
        @AuthenticationPrincipal accountPrincipal: AccountPrincipal,
    ): ResponseEntity<ApiResponse<CreateMerchantResponse>> {
        val appRequest = request.toAppRequest(accountPrincipal)
        val response = merchantService.createMerchant(appRequest)

        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @GetMapping
    fun getMerchants() {}

    @GetMapping("/{merchantId}")
    fun getMerchant(
        @PathVariable merchantId: String
    ) {}
}
