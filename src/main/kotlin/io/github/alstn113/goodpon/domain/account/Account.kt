package io.github.alstn113.goodpon.domain.account

data class Account(
    val id: Long,
    val email: String,
    val password: String,
    val name: String,
)
