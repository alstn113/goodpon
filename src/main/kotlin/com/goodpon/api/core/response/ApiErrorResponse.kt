package com.goodpon.api.core.response

import com.goodpon.goodpon.support.error.ErrorMessage
import com.goodpon.goodpon.support.error.ErrorType

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
                error = ErrorMessage(errorType),
            )
        }
    }
}
