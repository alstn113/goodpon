package com.goodpon.partner.openapi.response

import com.goodpon.domain.support.error.ErrorMessage
import com.goodpon.domain.support.error.ErrorType


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
