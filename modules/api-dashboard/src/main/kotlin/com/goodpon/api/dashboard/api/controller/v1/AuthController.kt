package com.goodpon.api.dashboard.api.controller.v1

import com.goodpon.core.application.auth.request.LoginRequest
import com.goodpon.core.application.auth.request.ResendVerificationEmailRequest
import com.goodpon.core.application.auth.request.VerifyEmailRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController {

    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest,
    ) {
    }

    @PostMapping("/verify")
    fun verifyEmail(
        @RequestBody request: VerifyEmailRequest,
    ) {
    }

    @PostMapping("/verify/resend")
    fun resendVerificationEmail(
        @RequestBody request: ResendVerificationEmailRequest,
    ) {
    }
}