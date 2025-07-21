package com.goodpon.api.dashboard.controller.v1.merchant

import com.goodpon.api.dashboard.controller.v1.merchant.dto.CreateMerchantRequest
import com.goodpon.api.dashboard.response.ApiResponse
import com.goodpon.api.dashboard.security.AccountPrincipal
import com.goodpon.application.dashboard.merchant.port.`in`.CreateMerchantUseCase
import com.goodpon.application.dashboard.merchant.port.`in`.GetMyMerchantDetailUseCase
import com.goodpon.application.dashboard.merchant.port.`in`.GetMyMerchantsUseCase
import com.goodpon.application.dashboard.merchant.port.`in`.dto.CreateMerchantResult
import com.goodpon.application.dashboard.merchant.service.dto.MyMerchantDetail
import com.goodpon.application.dashboard.merchant.service.dto.MyMerchantSummaries
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
class MerchantController(
    private val createMerchantUseCase: CreateMerchantUseCase,
    private val getMyMerchantsUseCase: GetMyMerchantsUseCase,
    private val getMyMerchantDetailUseCase: GetMyMerchantDetailUseCase,
) {

    @PostMapping("/v1/merchants")
    fun createMerchant(
        @RequestBody request: CreateMerchantRequest,
        @AuthenticationPrincipal accountPrincipal: AccountPrincipal,
    ): ResponseEntity<ApiResponse<CreateMerchantResult>> {
        val command = request.toCommand(accountPrincipal.id)
        val result = createMerchantUseCase(command)

        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @GetMapping("/v1/merchants")
    fun getMyMerchants(
        @AuthenticationPrincipal accountPrincipal: AccountPrincipal,
    ): ResponseEntity<ApiResponse<MyMerchantSummaries>> {
        val summaries = getMyMerchantsUseCase(accountPrincipal.id)

        return ResponseEntity.ok(ApiResponse.success(summaries))
    }

    @GetMapping("/v1/merchants/{merchantId}")
    fun getMyMerchantDetail(
        @PathVariable merchantId: Long,
        @AuthenticationPrincipal accountPrincipal: AccountPrincipal,
    ): ResponseEntity<ApiResponse<MyMerchantDetail>> {
        val detail = getMyMerchantDetailUseCase(
            accountId = accountPrincipal.id,
            merchantId = merchantId,
        )

        return ResponseEntity.ok(ApiResponse.success(detail))
    }
}
