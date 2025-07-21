package com.goodpon.application.dashboard.account.port.`in`.dto

data class SignUpCommand(
    val email: String,
    val password: String,
    val name: String,
)
