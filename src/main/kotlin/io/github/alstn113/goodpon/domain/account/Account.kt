package io.github.alstn113.goodpon.domain.account

import java.time.LocalDateTime

data class Account(
    val id: Long?,
    val email: String,
    val password: String,
    val name: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)