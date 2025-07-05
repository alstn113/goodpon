package com.goodpon.domain.application.auth.event

data class ResendVerificationEmailEvent(
    val accountId: Long,
    val email: String,
    val name: String,
)