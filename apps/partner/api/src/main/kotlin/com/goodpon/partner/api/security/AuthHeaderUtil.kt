package com.goodpon.partner.api.security

import jakarta.servlet.http.HttpServletRequest

object AuthHeaderUtil {

    fun extractClientId(request: HttpServletRequest): String? {
        return extractHeader(request, ApiKeyHeader.CLIENT_ID.headerName)
    }

    fun extractClientSecret(request: HttpServletRequest): String? {
        return extractHeader(request, ApiKeyHeader.CLIENT_SECRET.headerName)
    }

    private fun extractHeader(request: HttpServletRequest, headerName: String): String? {
        val header = request.getHeader(headerName)
        if (header.isNullOrBlank()) {
            return null
        }
        return header.trim()
    }
}