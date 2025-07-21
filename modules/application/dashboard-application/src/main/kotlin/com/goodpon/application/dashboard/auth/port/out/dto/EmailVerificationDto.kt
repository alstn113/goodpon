package com.goodpon.application.dashboard.auth.port.out.dto

import java.time.LocalDateTime

data class EmailVerificationDto(
    val accountId: Long,
    val token: String,
    val email: String,
    val name: String,
    val createdAt: LocalDateTime,
)
