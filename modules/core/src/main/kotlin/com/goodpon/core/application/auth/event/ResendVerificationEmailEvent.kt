package com.goodpon.core.application.auth.event

data class ResendVerificationEmailEvent(
    val accountId: Long,
    val email: String,
    val name: String,
)