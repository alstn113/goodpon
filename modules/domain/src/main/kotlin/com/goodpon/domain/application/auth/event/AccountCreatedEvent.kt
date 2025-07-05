package com.goodpon.domain.application.auth.event

data class AccountCreatedEvent(
    val accountId: Long,
    val email: String,
    val name: String,
)