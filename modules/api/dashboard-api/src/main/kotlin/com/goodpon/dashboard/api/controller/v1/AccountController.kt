package com.goodpon.dashboard.api.controller.v1

import com.goodpon.api.dashboard.api.response.ApiResponse
import com.goodpon.api.dashboard.security.AccountPrincipal
import com.goodpon.domain.application.account.AccountService
import com.goodpon.domain.application.account.response.AccountInfo
import com.goodpon.domain.application.auth.request.SignUpRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AccountController(
    private val accountService: AccountService,
) {

    @PostMapping("/v1/account/sign-up")
    fun signUp(
        @RequestBody request: SignUpRequest,
    ): ResponseEntity<ApiResponse<AccountInfo>> {
        val accountInfo = accountService.signUp(request)

        return ResponseEntity.ok(ApiResponse.success(accountInfo))
    }

    @GetMapping("/v1/account")
    fun getAccountInfo(
        @AuthenticationPrincipal principal: AccountPrincipal,
    ): ResponseEntity<ApiResponse<AccountInfo>> {
        val accountInfo = accountService.getAccountInfo(principal.id)

        return ResponseEntity.ok(ApiResponse.success(accountInfo))
    }
}
