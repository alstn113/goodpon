package com.goodpon.application.partner.idempotency.service

import com.goodpon.application.partner.idempotency.port.`in`.IdempotencyUseCase
import com.goodpon.application.partner.idempotency.port.out.IdempotencyCachePort
import org.springframework.stereotype.Service

@Service
class IdempotencyService(
    private val idempotencyCachePort: IdempotencyCachePort,
) : IdempotencyUseCase {

    override fun validateKey(key: String, requestHash: String): IdempotencyCheckResult {
        return idempotencyCachePort.validateKey(key = key, requestHash = requestHash)
    }

    override fun markAsProcessing(key: String, requestHash: String) {
        idempotencyCachePort.markAsProcessing(key = key, requestHash = requestHash)
    }

    override fun markAsCompleted(key: String, response: IdempotencyResponse) {
        idempotencyCachePort.markAsCompleted(key = key, response = response);
    }

    override fun clearProcessing(key: String) {
        idempotencyCachePort.clearProcessing(key = key)
    }
}