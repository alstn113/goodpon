package com.goodpon.application.dashboard.auth.port.out.dto

data class SendVerificationEmailRequest(
    val name: String,
    val email: String,
    val verificationLink: String,
)
