package com.goodpon.dashboard.application.auth.port.`in`

import com.goodpon.dashboard.application.auth.port.`in`.dto.LoginCommand
import com.goodpon.dashboard.application.auth.port.`in`.dto.LoginResult

interface LoginUseCase {

    fun login(command: LoginCommand): LoginResult
}