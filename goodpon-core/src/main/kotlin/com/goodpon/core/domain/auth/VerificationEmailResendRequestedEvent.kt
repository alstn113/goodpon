package com.goodpon.core.domain.auth

data class VerificationEmailResendRequestedEvent(
    val accountId: Long,
    val email: String,
    val name: String,
)