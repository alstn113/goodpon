package com.goodpon.common.application.auth.request

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String,
)
