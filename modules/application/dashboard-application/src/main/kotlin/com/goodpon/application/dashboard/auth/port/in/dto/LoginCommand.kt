package com.goodpon.application.dashboard.auth.port.`in`.dto


data class LoginCommand(
    val email: String,
    val password: String,
)