package com.goodpon.core.domain.auth

data class VerificationEmailRequestedEvent(
    val accountId: Long,
    val email: String,
    val name: String,
)