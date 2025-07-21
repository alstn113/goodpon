package com.goodpon.api.dashboard.controller.v1.account

import com.goodpon.api.dashboard.controller.v1.account.dto.SignUpRequest
import com.goodpon.api.dashboard.response.ApiResponse
import com.goodpon.api.dashboard.security.AccountPrincipal
import com.goodpon.application.dashboard.account.port.`in`.GetAccountInfoUseCase
import com.goodpon.application.dashboard.account.port.`in`.SignUpUseCase
import com.goodpon.application.dashboard.account.port.`in`.dto.AccountInfo
import com.goodpon.application.dashboard.account.port.`in`.dto.SignUpResult
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AccountController(
    private val signUpUseCase: SignUpUseCase,
    private val getAccountInfoUseCase: GetAccountInfoUseCase,
) {

    @PostMapping("/v1/accounts/sign-up")
    fun signUp(
        @RequestBody request: SignUpRequest,
    ): ResponseEntity<ApiResponse<SignUpResult>> {
        val command = request.toCommand()
        val result = signUpUseCase(command)

        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @GetMapping("/v1/accounts")
    fun getAccountInfo(
        @AuthenticationPrincipal principal: AccountPrincipal,
    ): ResponseEntity<ApiResponse<AccountInfo>> {
        val accountInfo = getAccountInfoUseCase(principal.id)

        return ResponseEntity.ok(ApiResponse.success(accountInfo))
    }
}
