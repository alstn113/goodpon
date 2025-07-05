package com.goodpon.core.application.auth.event

data class AccountCreatedEvent(
    val accountId: Long,
    val email: String,
    val name: String,
)