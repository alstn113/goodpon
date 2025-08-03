package com.goodpon.application.partner.idempotency.port.out

import com.goodpon.application.partner.idempotency.service.IdempotencyCheckResult

interface IdempotencyCachePort {

    fun check(key: String, requestHash: String): IdempotencyCheckResult

    fun saveProcessing(key: String, requestHash: String)

    fun saveCompleted(key: String, result: Any)
}