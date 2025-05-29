package io.github.alstn113.payments.support.response

import io.github.alstn113.payments.support.error.ErrorMessage
import io.github.alstn113.payments.support.error.ErrorType

data class ApiErrorResponse private constructor(
    val version: String,
    val traceId: String,
    val error: ErrorMessage,
) {

    companion object {
        fun of(traceId: String, errorType: ErrorType): ApiErrorResponse {
            return ApiErrorResponse(
                version = "2025-05-29",
                traceId = traceId,
                error = ErrorMessage(
                    code = errorType.name,
                    message = errorType.message,
                )
            )
        }
    }
}
