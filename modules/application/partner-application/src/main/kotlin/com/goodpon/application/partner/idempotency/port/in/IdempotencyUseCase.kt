package com.goodpon.application.partner.idempotency.port.`in`

import com.goodpon.application.partner.idempotency.service.IdempotencyCheckResult
import com.goodpon.application.partner.idempotency.service.IdempotencyResponse

interface IdempotencyUseCase {

    fun checkOrMarkAsProcessing(key: String, hashedRequestBody: String): IdempotencyCheckResult

    fun markAsCompleted(key: String, response: IdempotencyResponse)

    fun clearProcessing(key: String)
}
