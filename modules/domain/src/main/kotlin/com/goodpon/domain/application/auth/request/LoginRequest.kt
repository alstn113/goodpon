package com.goodpon.domain.application.auth.request

data class LoginRequest(
    val email: String,
    val password: String,
)
