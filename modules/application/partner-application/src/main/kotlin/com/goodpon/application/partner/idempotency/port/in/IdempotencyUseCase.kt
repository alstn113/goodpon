package com.goodpon.application.partner.idempotency.port.`in`

import com.goodpon.application.partner.idempotency.service.IdempotencyCheckResult
import com.goodpon.application.partner.idempotency.service.IdempotencyResponse

interface IdempotencyUseCase {

    fun check(key: String, requestHash: String): IdempotencyCheckResult

    fun saveProcessing(key: String, requestHash: String)

    fun saveCompleted(key: String, response: IdempotencyResponse)
}
