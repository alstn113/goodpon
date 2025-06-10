package com.goodpon.api.dashboard.v1

import com.goodpon.goodpon.api.dashboard.response.ApiResponse
import com.goodpon.goodpon.application.auth.AuthFacadeService
import com.goodpon.goodpon.application.auth.request.RegisterRequest
import com.goodpon.application.auth.request.ResendVerificationEmailRequest
import com.goodpon.goodpon.application.auth.request.VerifyEmailRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/auth")
class AuthController(
    private val authFacadeService: AuthFacadeService,
) {

    @PostMapping("/register")
    fun registerAccount(
        @RequestBody request: RegisterRequest,
    ): ResponseEntity<ApiResponse<Unit>> {
        authFacadeService.registerAccount(request)

        return ResponseEntity.ok(ApiResponse.success())
    }

    @GetMapping("/verify")
    fun verifyEmail(
        @RequestBody request: VerifyEmailRequest,
    ): ResponseEntity<ApiResponse<Unit>> {
        authFacadeService.verifyEmail(request)

        return ResponseEntity.ok(ApiResponse.success())
    }

    @PostMapping("/verify/resend")
    fun resendVerificationEmail(
        @RequestBody request: com.goodpon.application.auth.request.ResendVerificationEmailRequest,
    ): ResponseEntity<ApiResponse<Unit>> {
        authFacadeService.resendVerificationEmail(request)

        return ResponseEntity.ok(ApiResponse.success())
    }
}
