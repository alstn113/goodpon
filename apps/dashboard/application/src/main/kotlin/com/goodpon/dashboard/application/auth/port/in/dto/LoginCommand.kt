package com.goodpon.dashboard.application.auth.port.`in`.dto


data class LoginCommand(
    val email: String,
    val password: String,
)