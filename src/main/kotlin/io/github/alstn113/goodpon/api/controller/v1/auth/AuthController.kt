package io.github.alstn113.goodpon.api.controller.v1.auth

import io.github.alstn113.goodpon.application.auth.AuthService
import io.github.alstn113.goodpon.application.auth.request.RegisterRequest
import io.github.alstn113.goodpon.application.auth.request.ResendVerificationEmailRequest
import io.github.alstn113.goodpon.application.auth.request.VerifyEmailRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/auth")
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest) {
        authService.register(request)
    }

    @GetMapping("/verify")
    fun verify(@RequestBody request: VerifyEmailRequest) {
        authService.verify(request)
    }

    // 인증 이메일 재전송
    @PostMapping("/verify/resend")
    fun resendVerificationEmail(
        @RequestBody request: ResendVerificationEmailRequest,
    ) {
        authService.resendVerificationEmail(request)
    }
}
