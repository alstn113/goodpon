package com.goodpon.dashboard.application.auth.service.request

data class LoginRequest(
    val email: String,
    val password: String,
)
