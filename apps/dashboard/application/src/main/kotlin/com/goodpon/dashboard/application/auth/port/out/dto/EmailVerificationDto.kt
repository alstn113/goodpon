package com.goodpon.dashboard.application.auth.port.out.dto

import java.time.LocalDateTime

data class EmailVerificationDto(
    val accountId: Long,
    val token: String,
    val email: String,
    val name: String,
    val createdAt: LocalDateTime,
)
