package com.goodpon.application.dashboard.auth.service.event

data class ResendVerificationEmailEvent(
    val accountId: Long,
    val email: String,
    val name: String,
)