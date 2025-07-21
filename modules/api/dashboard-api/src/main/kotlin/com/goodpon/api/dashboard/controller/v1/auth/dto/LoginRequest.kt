package com.goodpon.api.dashboard.controller.v1.auth.dto

import com.goodpon.application.dashboard.auth.port.`in`.dto.LoginCommand

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
