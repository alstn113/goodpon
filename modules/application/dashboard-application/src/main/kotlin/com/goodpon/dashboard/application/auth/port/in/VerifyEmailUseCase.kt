package com.goodpon.dashboard.application.auth.port.`in`

fun interface VerifyEmailUseCase {

    operator fun invoke(token: String)
}