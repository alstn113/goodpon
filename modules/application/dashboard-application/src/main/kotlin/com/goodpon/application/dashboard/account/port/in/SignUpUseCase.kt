package com.goodpon.application.dashboard.account.port.`in`

import com.goodpon.application.dashboard.account.port.`in`.dto.SignUpCommand
import com.goodpon.application.dashboard.account.port.`in`.dto.SignUpResult

fun interface SignUpUseCase {

    operator fun invoke(command: SignUpCommand): SignUpResult
}