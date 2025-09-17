package com.goodpon.application.partner.idempotency.service

import com.goodpon.application.partner.idempotency.port.`in`.IdempotencyUseCase
import com.goodpon.application.partner.idempotency.port.out.IdempotencyCachePort
import org.springframework.stereotype.Service

@Service
class IdempotencyService(
    private val idempotencyCachePort: IdempotencyCachePort,
) : IdempotencyUseCase {

    override fun checkOrMarkAsProcessing(key: String, hashedRequestBody: String): IdempotencyCheckResult {
        return idempotencyCachePort.checkOrMarkAsProcessing(key = key, hashedRequestBody = hashedRequestBody)
    }

    override fun markAsCompleted(key: String, response: IdempotencyResponse) {
        idempotencyCachePort.markAsCompleted(key = key, response = response);
    }

    override fun clearProcessing(key: String) {
        idempotencyCachePort.clearProcessing(key = key)
    }
}