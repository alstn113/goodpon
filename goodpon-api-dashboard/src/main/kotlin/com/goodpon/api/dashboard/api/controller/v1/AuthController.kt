package com.goodpon.api.dashboard.api.controller.v1

import com.goodpon.api.dashboard.api.response.ApiResponse
import com.goodpon.common.application.auth.AuthFacadeService
import com.goodpon.common.application.auth.request.RegisterRequest
import com.goodpon.common.application.auth.request.VerificationEmailResendRequest
import com.goodpon.common.application.auth.request.EmailVerifyRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/auth")
class AuthController(
    private val authFacadeService: AuthFacadeService,
) {

    @PostMapping("/register")
    fun register(
        @RequestBody request: RegisterRequest,
    ): ResponseEntity<ApiResponse<Unit>> {
        authFacadeService.register(request)

        return ResponseEntity.ok(ApiResponse.success())
    }

    @GetMapping("/verify")
    fun verifyEmail(
        @RequestBody request: EmailVerifyRequest,
    ): ResponseEntity<ApiResponse<Unit>> {
        authFacadeService.verifyEmail(request)

        return ResponseEntity.ok(ApiResponse.success())
    }

    @PostMapping("/verify/resend")
    fun resendVerificationEmail(
        @RequestBody request: VerificationEmailResendRequest,
    ): ResponseEntity<ApiResponse<Unit>> {
        authFacadeService.resendVerificationEmail(request)

        return ResponseEntity.ok(ApiResponse.success())
    }
}
