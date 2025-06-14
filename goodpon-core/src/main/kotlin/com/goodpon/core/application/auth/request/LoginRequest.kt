package com.goodpon.core.application.auth.request

data class LoginRequest(
    val email: String,
    val password: String,
)
