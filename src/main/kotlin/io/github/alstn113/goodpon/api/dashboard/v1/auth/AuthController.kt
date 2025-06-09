package io.github.alstn113.goodpon.api.dashboard.v1.auth

import io.github.alstn113.goodpon.application.auth.AuthFacadeService
import io.github.alstn113.goodpon.application.auth.request.RegisterRequest
import io.github.alstn113.goodpon.application.auth.request.ResendVerificationEmailRequest
import io.github.alstn113.goodpon.application.auth.request.VerifyEmailRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/auth")
class AuthController(
    private val authFacadeService: AuthFacadeService,
) {

    @PostMapping("/register")
    fun registerAccount(@RequestBody request: RegisterRequest) {
        authFacadeService.registerAccount(request)
    }

    @GetMapping("/verify")
    fun verifyEmail(@RequestBody request: VerifyEmailRequest) {
        authFacadeService.verifyEmail(request)
    }

    @PostMapping("/verify/resend")
    fun resendVerificationEmail(@RequestBody request: ResendVerificationEmailRequest) {
        authFacadeService.resendVerificationEmail(request)
    }
}
