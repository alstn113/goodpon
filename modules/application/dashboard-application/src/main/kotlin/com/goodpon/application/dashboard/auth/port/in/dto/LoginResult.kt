package com.goodpon.application.dashboard.auth.port.`in`.dto

data class LoginResult(
    val id: Long,
    val email: String,
    val name: String,
    val verified: Boolean,
    val accessToken: String,
)