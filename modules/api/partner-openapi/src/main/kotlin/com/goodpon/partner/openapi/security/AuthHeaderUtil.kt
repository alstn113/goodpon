package com.goodpon.partner.openapi.security

import jakarta.servlet.http.HttpServletRequest

object AuthHeaderUtil {

    private const val CLIENT_ID_HEADER = "X-Goodpon-Client-Id"
    private const val CLIENT_SECRET_PREFIX = "X-Goodpon-Client-Secret"

    fun extractClientId(request: HttpServletRequest): String? {
        return extractHeader(request, CLIENT_ID_HEADER)
    }

    fun extractClientSecret(request: HttpServletRequest): String? {
        return extractHeader(request, CLIENT_SECRET_PREFIX)
    }

    private fun extractHeader(request: HttpServletRequest, headerName: String): String? {
        val header = request.getHeader(headerName)
        if (header.isNullOrBlank()) {
            return null
        }
        return header.trim()
    }
}