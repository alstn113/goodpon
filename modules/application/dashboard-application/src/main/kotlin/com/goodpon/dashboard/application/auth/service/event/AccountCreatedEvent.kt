package com.goodpon.dashboard.application.auth.service.event

data class AccountCreatedEvent(
    val accountId: Long,
    val email: String,
    val name: String,
)