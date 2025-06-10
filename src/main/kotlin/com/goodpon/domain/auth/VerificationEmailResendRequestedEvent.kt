package com.goodpon.domain.auth

data class VerificationEmailResendRequestedEvent(
    val accountId: Long,
    val email: String,
    val name: String,
)