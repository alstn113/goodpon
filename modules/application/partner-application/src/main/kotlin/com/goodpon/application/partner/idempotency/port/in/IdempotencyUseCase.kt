package com.goodpon.application.partner.idempotency.port.`in`

import com.goodpon.application.partner.idempotency.service.IdempotencyCheckResult
import com.goodpon.application.partner.idempotency.service.IdempotencyResponse

interface IdempotencyUseCase {

    fun validateKey(key: String, requestHash: String): IdempotencyCheckResult

    fun markAsProcessing(key: String, requestHash: String)

    fun markAsCompleted(key: String, response: IdempotencyResponse)

    fun clearProcessing(key: String)
}
