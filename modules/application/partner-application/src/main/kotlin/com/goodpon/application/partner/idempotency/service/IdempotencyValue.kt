package com.goodpon.application.partner.idempotency.service

data class IdempotencyValue(
    val requestHash: String,
    val status: IdempotencyStatus,
    val result: Any?,
)