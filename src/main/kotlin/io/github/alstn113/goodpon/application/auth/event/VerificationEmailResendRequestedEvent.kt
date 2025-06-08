package io.github.alstn113.goodpon.application.auth.event

data class VerificationEmailResendRequestedEvent(
    val accountId: Long,
    val email: String,
    val name: String,
)