package com.goodpon.infra.security.util

import com.goodpon.core.support.error.CoreException
import com.goodpon.core.support.error.ErrorType
import jakarta.servlet.http.HttpServletRequest
import java.util.*

object AuthHeaderUtil {

    private const val AUTHORIZATION_HEADER = "Authorization"
    private const val BEARER_PREFIX = "Bearer "
    private const val BASIC_PREFIX = "Basic "

    fun extractBearerToken(request: HttpServletRequest): String? {
        return extractToken(request, BEARER_PREFIX)
    }

    fun extractBasicAuthUsername(request: HttpServletRequest): String? {
        val basic64Token = extractToken(request, BASIC_PREFIX)
        if (basic64Token.isNullOrBlank()) return null

        val decodedToken = decodeBasicToken(basic64Token)
        val parts = decodedToken.split(":", limit = 2)
        return parts[0]
    }

    private fun extractToken(request: HttpServletRequest, prefix: String): String? {
        val authHeader = request.getHeader(AUTHORIZATION_HEADER)
        if (authHeader.isNullOrBlank()) return null
        if (!authHeader.startsWith(prefix)) return null

        val token = authHeader.removePrefix(prefix).trim()
        return token.ifBlank { null }
    }

    private fun decodeBasicToken(token: String): String {
        return try {
            String(Base64.getDecoder().decode(token))
        } catch (e: IllegalArgumentException) {
            throw CoreException(ErrorType.INCORRECT_BASIC_AUTH_FORMAT)
        }
    }
}