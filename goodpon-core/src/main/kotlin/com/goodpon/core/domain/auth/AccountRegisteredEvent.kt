package com.goodpon.core.domain.auth

data class AccountRegisteredEvent(
    val accountId: Long,
    val email: String,
    val name: String,
)