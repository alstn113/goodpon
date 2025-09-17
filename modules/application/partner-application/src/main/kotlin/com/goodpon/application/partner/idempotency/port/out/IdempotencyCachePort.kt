package com.goodpon.application.partner.idempotency.port.out

import com.goodpon.application.partner.idempotency.service.IdempotencyCheckResult
import com.goodpon.application.partner.idempotency.service.IdempotencyResponse

interface IdempotencyCachePort {

    fun checkOrMarkAsProcessing(key: String, hashedRequestBody: String): IdempotencyCheckResult

    fun markAsCompleted(key: String, response: IdempotencyResponse)

    fun clearProcessing(key: String)
}