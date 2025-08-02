package com.goodpon.application.partner.idempotency.port.`in`

sealed class IdempotencyCheckResult {
    object NotExists : IdempotencyCheckResult()
    object Processing : IdempotencyCheckResult()
    object Conflict : IdempotencyCheckResult()
    data class Completed(val result: Any) : IdempotencyCheckResult()
}