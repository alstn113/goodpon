package com.goodpon.dashboard.api.controller.v1.account

import com.goodpon.dashboard.api.response.ApiResponse
import com.goodpon.dashboard.api.security.AccountPrincipal
import com.goodpon.dashboard.application.account.port.`in`.dto.AccountInfo
import com.goodpon.dashboard.application.account.port.`in`.dto.SignUpResult
import com.goodpon.dashboard.application.account.service.AccountService
import com.goodpon.dashboard.application.auth.service.request.SignUpRequest
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
    ): ResponseEntity<ApiResponse<SignUpResult>> {
        val command = request.toCommand()
        val result = accountService.signUp(command)

        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @GetMapping("/v1/account")
    fun getAccountInfo(
        @AuthenticationPrincipal principal: AccountPrincipal,
    ): ResponseEntity<ApiResponse<AccountInfo>> {
        val accountInfo = accountService.getAccountInfo(principal.id)

        return ResponseEntity.ok(ApiResponse.success(accountInfo))
    }
}
