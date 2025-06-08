package io.github.alstn113.goodpon.api.controller.v1.auth

import io.github.alstn113.goodpon.application.auth.AuthService
import io.github.alstn113.goodpon.application.auth.request.RegisterRequest
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
    fun verify(@RequestParam("code") code: String) {
        // This endpoint is used to verify the email address of the user
    }
}
