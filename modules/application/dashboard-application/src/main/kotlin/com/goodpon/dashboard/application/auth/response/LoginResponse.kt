package com.goodpon.dashboard.application.auth.response

data class LoginResponse(
    val id: Long,
    val email: String,
    val name: String,
    val verified: Boolean,
    val accessToken: String,
)
