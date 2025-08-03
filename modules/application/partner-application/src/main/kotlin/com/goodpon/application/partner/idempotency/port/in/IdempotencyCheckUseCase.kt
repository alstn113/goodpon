package com.goodpon.application.partner.idempotency.port.`in`

import com.goodpon.application.partner.idempotency.service.IdempotencyCheckResult

fun interface IdempotencyCheckUseCase {

    operator fun invoke(key: String, requestHash: String): IdempotencyCheckResult
}
