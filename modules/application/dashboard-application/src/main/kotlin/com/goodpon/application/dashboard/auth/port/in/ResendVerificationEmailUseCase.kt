package com.goodpon.application.dashboard.auth.port.`in`

fun interface ResendVerificationEmailUseCase {

    operator fun invoke(email: String)
}