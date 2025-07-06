package com.goodpon.dashboard.application.auth.request

data class SignUpRequest(
    val email: String,
    val password: String,
    val name: String,
)
