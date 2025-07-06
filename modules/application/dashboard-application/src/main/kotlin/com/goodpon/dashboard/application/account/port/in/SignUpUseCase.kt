package com.goodpon.dashboard.application.account.port.`in`

import com.goodpon.dashboard.application.account.port.`in`.dto.SignUpCommand
import com.goodpon.dashboard.application.account.port.`in`.dto.SignUpResult

interface SignUpUseCase {

    fun signUp(command: SignUpCommand): SignUpResult
}