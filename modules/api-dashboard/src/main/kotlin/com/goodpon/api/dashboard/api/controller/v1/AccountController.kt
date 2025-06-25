package com.goodpon.api.dashboard.api.controller.v1

import com.goodpon.core.application.auth.AuthFacadeService
import com.goodpon.core.application.auth.request.SignUpRequest
import com.goodpon.core.domain.auth.AccountPrincipal
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/account")
class AccountController(
    private val authFacadeService: AuthFacadeService,
) {

    @PostMapping("/sign-up")
    fun signUp(
        @RequestBody request: SignUpRequest,
    ) {
    }

    @GetMapping
    fun getAccountInfo(
        @AuthenticationPrincipal principal: AccountPrincipal,
    ) {
    }
}
