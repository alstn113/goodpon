package com.goodpon.partner.application.idempotency.service

import com.goodpon.partner.application.idempotency.port.`in`.IdempotencyUseCase
import com.goodpon.partner.application.idempotency.port.out.IdempotencyStore
import org.springframework.stereotype.Service

@Service
class IdempotencyService(
    private val idempotencyStore: IdempotencyStore,
) : IdempotencyUseCase {

    override fun checkOrMarkAsProcessing(key: String, hashedRequestBody: String): IdempotencyCheckResult {
        return idempotencyStore.checkOrMarkAsProcessing(key = key, hashedRequestBody = hashedRequestBody)
    }

    override fun markAsCompleted(key: String, response: IdempotencyResponse) {
        idempotencyStore.markAsCompleted(key = key, response = response);
    }

    override fun clearProcessing(key: String) {
        idempotencyStore.clearProcessing(key = key)
    }
}