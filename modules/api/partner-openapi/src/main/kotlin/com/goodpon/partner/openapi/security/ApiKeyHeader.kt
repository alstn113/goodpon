package com.goodpon.partner.openapi.security

enum class ApiKeyHeader(
    val headerName: String,
) {

    CLIENT_ID("X-Goodpon-Client-Id"),
    CLIENT_SECRET("X-Goodpon-Client-Secret");
}