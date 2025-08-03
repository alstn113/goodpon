package com.goodpon.application.partner.idempotency.service

data class IdempotencyEntry(
    val requestHash: String,
    val status: IdempotencyStatus,
    val response: IdempotencyResponse?,
)