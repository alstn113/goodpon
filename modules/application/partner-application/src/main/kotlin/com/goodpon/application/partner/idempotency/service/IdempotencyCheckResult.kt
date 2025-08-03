package com.goodpon.application.partner.idempotency.service

sealed class IdempotencyCheckResult {

    data object NotExists : IdempotencyCheckResult()
    data object Processing : IdempotencyCheckResult()
    data object Conflict : IdempotencyCheckResult()
    data class Completed(val result: Any) : IdempotencyCheckResult()
}