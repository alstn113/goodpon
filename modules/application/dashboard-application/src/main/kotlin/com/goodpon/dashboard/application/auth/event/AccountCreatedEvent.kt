package com.goodpon.dashboard.application.auth.event

data class AccountCreatedEvent(
    val accountId: Long,
    val email: String,
    val name: String,
)