package io.github.alstn113.goodpon.domain.auth

data class AccountRegisteredEvent(
    val accountId: Long,
    val email: String,
    val name: String,
)