package com.goodpon.api.dashboard.api.controller.v1

import com.goodpon.api.dashboard.api.response.ApiResponse
import com.goodpon.core.application.auth.AuthService
import com.goodpon.core.application.auth.request.LoginRequest
import com.goodpon.core.application.auth.request.ResendVerificationEmailRequest
import com.goodpon.core.application.auth.request.VerifyEmailRequest
import com.goodpon.core.application.auth.response.LoginResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/auth")
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest,
    ): ResponseEntity<ApiResponse<LoginResponse>> {
        val response = authService.login(request)

        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @PostMapping("/verify")
    fun verifyEmail(
        @RequestBody request: VerifyEmailRequest,
    ): ResponseEntity<ApiResponse<String>> {
        authService.verifyEmail(request.token)

        return ResponseEntity.ok(ApiResponse.success("이메일 인증이 완료되었습니다."))
    }

    @PostMapping("/verify/resend")
    fun resendVerificationEmail(
        @RequestBody request: ResendVerificationEmailRequest,
    ): ResponseEntity<ApiResponse<String>> {
        authService.resendVerificationEmail(request.email)

        return ResponseEntity.ok(ApiResponse.success("인증 이메일이 재전송되었습니다."))
    }
}