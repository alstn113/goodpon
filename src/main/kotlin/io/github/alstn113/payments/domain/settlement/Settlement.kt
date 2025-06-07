package io.github.alstn113.payments.domain.settlement

import java.time.LocalDateTime

data class Settlement(
    val id: String? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)