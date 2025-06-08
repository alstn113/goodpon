package io.github.alstn113.goodpon.application.auth

data class VerificationEmailResentEvent(
    val accountId: Long,
    val email: String,
    val name: String,
)
