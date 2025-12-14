package com.goodpon.partner.application.idempotency.port.`in`

import com.goodpon.partner.application.idempotency.service.IdempotencyCheckResult
import com.goodpon.partner.application.idempotency.service.IdempotencyResponse

interface IdempotencyUseCase {

    fun checkOrMarkAsProcessing(key: String, hashedRequestBody: String): IdempotencyCheckResult

    fun markAsCompleted(key: String, response: IdempotencyResponse)

    fun clearProcessing(key: String)
}
