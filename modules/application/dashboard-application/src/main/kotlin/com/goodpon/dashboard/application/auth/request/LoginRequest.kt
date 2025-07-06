package com.goodpon.dashboard.application.auth.request

data class LoginRequest(
    val email: String,
    val password: String,
)
