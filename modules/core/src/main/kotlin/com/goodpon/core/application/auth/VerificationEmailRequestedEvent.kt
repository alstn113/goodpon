package com.goodpon.core.application.auth

data class VerificationEmailRequestedEvent(
    val accountId: Long,
    val email: String,
    val name: String,
)