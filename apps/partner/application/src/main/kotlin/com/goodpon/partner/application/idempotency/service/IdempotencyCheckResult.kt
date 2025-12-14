package com.goodpon.partner.application.idempotency.service

sealed class IdempotencyCheckResult {

    data object FirstRequestProcessing : IdempotencyCheckResult()
    data object CurrentlyProcessing : IdempotencyCheckResult()
    data object RequestBodyMismatch : IdempotencyCheckResult()
    data class AlreadyCompleted(val response: IdempotencyResponse) : IdempotencyCheckResult()
}