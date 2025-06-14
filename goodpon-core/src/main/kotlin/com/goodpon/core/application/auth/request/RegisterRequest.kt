package com.goodpon.core.application.auth.request

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String,
)
