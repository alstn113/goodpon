package com.goodpon.api.dashboard.controller.v1.account.dto

import com.goodpon.application.dashboard.account.port.`in`.dto.SignUpCommand

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