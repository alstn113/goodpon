package com.goodpon.partner.application.idempotency.service

data class IdempotencyEntry(
    val requestHash: String,
    val status: IdempotencyStatus,
    val response: IdempotencyResponse?,
)