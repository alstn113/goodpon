package io.github.alstn113.goodpon.api.dashboard.v1

import io.github.alstn113.goodpon.api.dashboard.response.ApiResponse
import io.github.alstn113.goodpon.application.auth.AuthFacadeService
import io.github.alstn113.goodpon.application.auth.request.RegisterRequest
import io.github.alstn113.goodpon.application.auth.request.ResendVerificationEmailRequest
import io.github.alstn113.goodpon.application.auth.request.VerifyEmailRequest
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
        @RequestBody request: ResendVerificationEmailRequest,
    ): ResponseEntity<ApiResponse<Unit>> {
        authFacadeService.resendVerificationEmail(request)

        return ResponseEntity.ok(ApiResponse.success())
    }
}
