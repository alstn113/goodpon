package com.goodpon.core.domain.auth

import java.time.LocalDateTime

data class EmailVerification(
    val accountId: Long,
    val token: String,
    val email: String,
    val name: String,
    val createdAt: LocalDateTime,
)
