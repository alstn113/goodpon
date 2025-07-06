package com.goodpon.dashboard.application.auth.service.request

import com.goodpon.dashboard.application.account.port.`in`.dto.SignUpCommand

data class SignUpRequest(
    val email: String,
    val password: String,
    val name: String,
) {

    fun toCommand(): SignUpCommand {
        return SignUpCommand(
            email = email,
            password = password,
            name = name
        )
    }
}
