package com.goodpon.application.partner.idempotency.port.`in`

fun interface IdempotencySaveCompletedUseCase {

    operator fun invoke(key: String, result: Any)
}