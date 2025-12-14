package com.goodpon.partner.application.idempotency.port.out

import com.goodpon.partner.application.idempotency.service.IdempotencyCheckResult
import com.goodpon.partner.application.idempotency.service.IdempotencyResponse

interface IdempotencyCachePort {

    fun checkOrMarkAsProcessing(key: String, hashedRequestBody: String): IdempotencyCheckResult

    fun markAsCompleted(key: String, response: IdempotencyResponse)

    fun clearProcessing(key: String)
}