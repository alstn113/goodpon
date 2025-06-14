package com.goodpon.common.application.auth.request

data class LoginRequest(
    val email: String,
    val password: String,
)
