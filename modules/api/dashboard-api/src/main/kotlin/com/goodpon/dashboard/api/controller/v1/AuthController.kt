package com.goodpon.dashboard.api.controller.v1

import com.goodpon.api.dashboard.api.response.ApiResponse
import com.goodpon.domain.application.auth.AuthService
import com.goodpon.domain.application.auth.request.LoginRequest
import com.goodpon.domain.application.auth.request.ResendVerificationEmailRequest
import com.goodpon.domain.application.auth.request.VerifyEmailRequest
import com.goodpon.domain.application.auth.response.LoginResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping("/v1/auth/login")
    fun login(
        @RequestBody request: LoginRequest,
    ): ResponseEntity<ApiResponse<LoginResponse>> {
        val response = authService.login(request)

        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @PostMapping("/v1/auth/verify")
    fun verifyEmail(
        @RequestBody request: VerifyEmailRequest,
    ): ResponseEntity<ApiResponse<String>> {
        authService.verifyEmail(request.token)

        return ResponseEntity.ok(ApiResponse.success("이메일 인증이 완료되었습니다."))
    }

    @PostMapping("/v1/auth/verify/resend")
    fun resendVerificationEmail(
        @RequestBody request: ResendVerificationEmailRequest,
    ): ResponseEntity<ApiResponse<String>> {
        authService.resendVerificationEmail(request.email)

        return ResponseEntity.ok(ApiResponse.success("인증 이메일이 재전송되었습니다."))
    }
}