package com.goodpon.application.dashboard.auth.port.`in`

fun interface VerifyEmailUseCase {

    operator fun invoke(token: String)
}