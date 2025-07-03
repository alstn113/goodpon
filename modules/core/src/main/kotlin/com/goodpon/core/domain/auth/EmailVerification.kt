package com.goodpon.core.domain.auth

import java.time.LocalDateTime

data class EmailVerification private constructor(
    val accountId: Long,
    val token: String,
    val email: String,
    val name: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    companion object {
        fun create(accountId: Long, token: String, email: String, name: String): EmailVerification {
            return EmailVerification(
                accountId = accountId,
                token = token,
                email = email,
                name = name
            )
        }
    }
}
