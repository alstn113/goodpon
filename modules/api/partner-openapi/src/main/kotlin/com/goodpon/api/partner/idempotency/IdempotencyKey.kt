package com.goodpon.api.partner.idempotency

data class IdempotencyRecord(
    val status: IdempotencyStatus,
    val requestHash: String,
    val responseBodyJson: String,
)

enum class IdempotencyStatus {
    PROCESSING,
    COMPLETED,
}