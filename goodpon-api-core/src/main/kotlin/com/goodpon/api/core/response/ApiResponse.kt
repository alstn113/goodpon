package com.goodpon.api.core.response

data class ApiResponse<T> private constructor(
    val version: String,
    val traceId: String,
    val entityType: String,
    val entityBody: T,
) {
    companion object {
        fun <T> of(entityType: String, entityBody: T): ApiResponse<T> {
            return ApiResponse(
                version = "2025-05-29",
                traceId = "",
                entityType = entityType,
                entityBody = entityBody,
            )
        }
    }
}
