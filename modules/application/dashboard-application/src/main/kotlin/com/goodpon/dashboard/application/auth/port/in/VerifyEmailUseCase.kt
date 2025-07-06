package com.goodpon.dashboard.application.auth.port.`in`

interface VerifyEmailUseCase {

    fun verifyEmail(token: String)
}