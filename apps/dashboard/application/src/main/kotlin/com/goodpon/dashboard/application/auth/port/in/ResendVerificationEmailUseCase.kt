package com.goodpon.dashboard.application.auth.port.`in`

fun interface ResendVerificationEmailUseCase {

    operator fun invoke(email: String)
}