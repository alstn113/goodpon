package com.goodpon.application.partner.idempotency.port.`in`

fun interface IdempotencySaveProcessingUseCase {

    operator fun invoke(key: String, requestHash: String)
}