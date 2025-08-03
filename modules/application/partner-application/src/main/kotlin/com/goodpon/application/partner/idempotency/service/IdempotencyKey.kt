package com.goodpon.application.partner.idempotency.service

data class IdempotencyKey(
    val key: String,
    val requestHash: String,
    val status: IdempotencyStatus,
)
