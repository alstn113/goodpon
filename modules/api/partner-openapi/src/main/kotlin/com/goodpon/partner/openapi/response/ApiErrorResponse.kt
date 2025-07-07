package com.goodpon.partner.openapi.response


data class ApiErrorResponse private constructor(
    val version: String,
    val traceId: String,
    val errorMessage: String,
) {

    companion object {
        fun of(traceId: String, errorMessage: String): ApiErrorResponse {
            return ApiErrorResponse(
                version = "2025-05-29",
                traceId = traceId,
                errorMessage = errorMessage
            )
        }
    }
}
