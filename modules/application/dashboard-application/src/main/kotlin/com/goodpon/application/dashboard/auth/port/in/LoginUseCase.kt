package com.goodpon.application.dashboard.auth.port.`in`

import com.goodpon.application.dashboard.auth.port.`in`.dto.LoginCommand
import com.goodpon.application.dashboard.auth.port.`in`.dto.LoginResult

fun interface LoginUseCase {

    operator fun invoke(command: LoginCommand): LoginResult
}