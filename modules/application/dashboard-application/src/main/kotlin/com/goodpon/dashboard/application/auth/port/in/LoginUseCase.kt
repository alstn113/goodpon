package com.goodpon.dashboard.application.auth.port.`in`

import com.goodpon.dashboard.application.auth.port.`in`.dto.LoginCommand
import com.goodpon.dashboard.application.auth.port.`in`.dto.LoginResult

fun interface LoginUseCase {

    operator fun invoke(command: LoginCommand): LoginResult
}