package com.goodpon.dashboard.api.controller.v1.auth.dto

import com.goodpon.dashboard.application.auth.port.`in`.dto.LoginCommand

data class LoginRequest(
    val email: String,
    val password: String,
) {

    fun toCommand(): LoginCommand {
        return LoginCommand(
            email = email,
            password = password,
        )
    }
}
