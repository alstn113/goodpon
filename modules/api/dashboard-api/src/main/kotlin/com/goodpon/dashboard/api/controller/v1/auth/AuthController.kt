package com.goodpon.dashboard.api.controller.v1.auth

import com.goodpon.dashboard.api.controller.v1.auth.dto.LoginRequest
import com.goodpon.dashboard.api.controller.v1.auth.dto.ResendVerificationEmailRequest
import com.goodpon.dashboard.api.controller.v1.auth.dto.VerifyEmailRequest
import com.goodpon.dashboard.api.response.ApiResponse
import com.goodpon.dashboard.application.auth.port.`in`.LoginUseCase
import com.goodpon.dashboard.application.auth.port.`in`.ResendVerificationEmailUseCase
import com.goodpon.dashboard.application.auth.port.`in`.VerifyEmailUseCase
import com.goodpon.dashboard.application.auth.port.`in`.dto.LoginResult
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val loginUseCase: LoginUseCase,
    private val verifyEmailUseCase: VerifyEmailUseCase,
    private val resendVerificationEmailUseCase: ResendVerificationEmailUseCase,
) {

    @PostMapping("/v1/auth/login")
    fun login(
        @RequestBody request: LoginRequest,
    ): ResponseEntity<ApiResponse<LoginResult>> {
        val command = request.toCommand()
        val result = loginUseCase.login(command)

        return ResponseEntity.ok(ApiResponse.success(result))
    }

    @PostMapping("/v1/auth/verify")
    fun verifyEmail(
        @RequestBody request: VerifyEmailRequest,
    ): ResponseEntity<ApiResponse<String>> {
        verifyEmailUseCase.verifyEmail(request.token)

        return ResponseEntity.ok(ApiResponse.success("이메일 인증이 완료되었습니다."))
    }

    @PostMapping("/v1/auth/verify/resend")
    fun resendVerificationEmail(
        @RequestBody request: ResendVerificationEmailRequest,
    ): ResponseEntity<ApiResponse<String>> {
        resendVerificationEmailUseCase.resendVerificationEmail(request.email)

        return ResponseEntity.ok(ApiResponse.success("인증 이메일이 재전송되었습니다."))
    }
}