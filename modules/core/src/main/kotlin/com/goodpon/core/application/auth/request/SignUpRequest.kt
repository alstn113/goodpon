package com.goodpon.core.application.auth.request

data class SignUpRequest(
    val email: String,
    val password: String,
    val name: String,
)
