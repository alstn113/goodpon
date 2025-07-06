package com.goodpon.partner.application.auth.event

data class ResendVerificationEmailEvent(
    val accountId: Long,
    val email: String,
    val name: String,
)