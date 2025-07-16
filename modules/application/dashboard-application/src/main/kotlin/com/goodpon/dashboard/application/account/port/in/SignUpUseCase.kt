package com.goodpon.dashboard.application.account.port.`in`

import com.goodpon.dashboard.application.account.port.`in`.dto.SignUpCommand
import com.goodpon.dashboard.application.account.port.`in`.dto.SignUpResult

fun interface SignUpUseCase {

    operator fun invoke(command: SignUpCommand): SignUpResult
}