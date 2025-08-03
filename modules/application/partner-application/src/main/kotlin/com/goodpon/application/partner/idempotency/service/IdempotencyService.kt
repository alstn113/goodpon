package com.goodpon.application.partner.idempotency.service

import com.goodpon.application.partner.idempotency.port.`in`.IdempotencyUseCase
import com.goodpon.application.partner.idempotency.port.out.IdempotencyCachePort
import org.springframework.stereotype.Service

@Service
class IdempotencyService(
    private val idempotencyCachePort: IdempotencyCachePort,
) : IdempotencyUseCase {

    override fun check(key: String, requestHash: String): IdempotencyCheckResult {
        return idempotencyCachePort.check(key = key, requestHash = requestHash)
    }

    override fun saveProcessing(key: String, requestHash: String) {
        idempotencyCachePort.saveProcessing(key = key, requestHash = requestHash)
    }

    override fun saveCompleted(key: String, response: IdempotencyResponse) {
        idempotencyCachePort.saveCompleted(key = key, response = response);
    }
}