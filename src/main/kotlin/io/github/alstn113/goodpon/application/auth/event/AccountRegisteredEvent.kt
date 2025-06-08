package io.github.alstn113.goodpon.application.auth.event

data class AccountRegisteredEvent(
    val accountId: Long,
    val email: String,
    val name: String,
)