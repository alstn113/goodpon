package com.goodpon.dashboard.api.controller.v1.merchant

import com.goodpon.dashboard.api.controller.v1.merchant.dto.CreateMerchantRequest
import com.goodpon.dashboard.api.response.ApiResponse
import com.goodpon.dashboard.api.security.AccountPrincipal
import com.goodpon.dashboard.application.merchant.port.`in`.CreateMerchantUseCase
import com.goodpon.dashboard.application.merchant.port.`in`.dto.CreateMerchantResult
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
class MerchantController(
    private val createMerchantUseCase: CreateMerchantUseCase,
) {

    @PostMapping("/v1/merchants")
    fun createMerchant(
        @RequestBody request: CreateMerchantRequest,
        @AuthenticationPrincipal accountPrincipal: AccountPrincipal,
    ): ResponseEntity<ApiResponse<CreateMerchantResult>> {
        val command = request.toCommand(accountPrincipal.id)
        val result = createMerchantUseCase.createMerchant(command)

        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @GetMapping("/v1/merchants")
    fun getMyMerchants(
        @AuthenticationPrincipal accountPrincipal: AccountPrincipal,
    ) {
    }

    @GetMapping("/v1/merchants/{merchantId}")
    fun getMyMerchant(
        @PathVariable merchantId: Long,
        @AuthenticationPrincipal accountPrincipal: AccountPrincipal,
    ) {
    }
}
