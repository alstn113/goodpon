package com.goodpon.dashboard.application.account.port.`in`.dto

data class SignUpCommand(
    val email: String,
    val password: String,
    val name: String,
)
