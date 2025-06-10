package com.goodpon.common.domain.auth

data class VerificationEmailResendRequestedEvent(
    val accountId: Long,
    val email: String,
    val name: String,
)